package com.ajou.travely.service;

import com.ajou.travely.controller.event.dto.EventCreateRequestDto;
import com.ajou.travely.controller.event.dto.EventResponseDto;
import com.ajou.travely.controller.event.dto.EventUpdateDto;
import com.ajou.travely.controller.event.dto.SimpleEventResponseDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.domain.Event;
import com.ajou.travely.domain.Notice;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.EventRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final PhotoService photoService;

    @Transactional
    public EventResponseDto createEvent(Long authorId, EventCreateRequestDto requestDto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        Event event = eventRepository.save(Event.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(author)
                .createdAt(LocalDateTime.now())
                .build());
        if (requestDto.getPhotos() != null) {
            photoService.createPhotosOfEvent(event, requestDto.getPhotos());
        }

        return new EventResponseDto(event);
    }

    @Transactional(readOnly = true)
    public EventResponseDto getEvent(Long eventId) {
        return new EventResponseDto(eventRepository.findById(eventId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 이벤트를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                )));
    }

    @Transactional(readOnly = true)
    public Page<SimpleEventResponseDto> getEvents(Pageable pageable) {
        return eventRepository
                .findAll(pageable)
                .map(SimpleEventResponseDto::new);
    }

    @Transactional
    public EventResponseDto updateEvent(Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 이벤트를 찾을 수 없습니다.",
                        ErrorCode.EVENT_NOT_FOUND
                ));
        if (eventUpdateDto.getPhotoIdsToRemove() != null) {
            photoService.removePhotoIds(eventUpdateDto.getPhotoIdsToRemove());
        }
        if (eventUpdateDto.getPhotos() != null) {
            photoService.createPhotosOfSomething(event, eventUpdateDto.getPhotos());
        }
        event.update(eventUpdateDto);
        return new EventResponseDto(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 이벤트를 찾을 수 없습니다.",
                        ErrorCode.EVENT_NOT_FOUND
                ));
        photoService.removePhotos(event.getPhotos());
        eventRepository.delete(event);
    }
}
