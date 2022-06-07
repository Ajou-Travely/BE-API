package com.ajou.travely.service;

import com.ajou.travely.controller.notice.dto.NoticeCreateRequestDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.controller.notice.dto.NoticeUpdateDto;
import com.ajou.travely.controller.notice.dto.SimpleNoticeResponseDto;
import com.ajou.travely.domain.Notice;
import com.ajou.travely.domain.Photo;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.NoticeRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {
    private final PhotoService photoService;

    private final NoticeRepository noticeRepository;

    private final UserRepository userRepository;

    @Transactional
    public NoticeResponseDto createNotice(Long authorId, NoticeCreateRequestDto requestDto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        Notice notice = noticeRepository.save(Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(author)
                .createdAt(LocalDateTime.now())
                .build());
        if (requestDto.getPhotos() != null) {
            if (!requestDto.getPhotos().isEmpty()) {
                photoService.createPhotosOfNotice(notice, requestDto.getPhotos());
            }
        }   
        
        return new NoticeResponseDto(notice);
    }

    @Transactional(readOnly = true)
    public NoticeResponseDto getNotice(Long noticeId) {
        return new NoticeResponseDto(noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 공지사항을 찾을 수 없습니다.",
                        ErrorCode.NOTICE_NOT_FOUND
                )));
    }

    @Transactional(readOnly = true)
    public Page<SimpleNoticeResponseDto> getNotices(Pageable pageable) {
        return noticeRepository
                .findAll(pageable)
                .map(SimpleNoticeResponseDto::new);
    }

    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeUpdateDto noticeUpdateDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 공지사항을 찾을 수 없습니다.",
                        ErrorCode.NOTICE_NOT_FOUND
                ));
        if (noticeUpdateDto.getPhotoIdsToRemove() != null) {
            photoService.removePhotoIds(noticeUpdateDto.getPhotoIdsToRemove());
        }
        if (noticeUpdateDto.getPhotos() != null) {
            photoService.createPhotosOfSomething(notice, noticeUpdateDto.getPhotos());
        }
        notice.update(noticeUpdateDto);
        return new NoticeResponseDto(notice);
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 공지사항을 찾을 수 없습니다.",
                        ErrorCode.NOTICE_NOT_FOUND
                ));
        photoService.removePhotos(notice.getPhotos());
        noticeRepository.delete(notice);
    }
}
