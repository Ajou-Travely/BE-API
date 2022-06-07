package com.ajou.travely.controller.admin;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.event.dto.EventCreateRequestDto;
import com.ajou.travely.controller.event.dto.EventResponseDto;
import com.ajou.travely.controller.event.dto.EventUpdateDto;
import com.ajou.travely.controller.event.dto.SimpleEventResponseDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.controller.notice.dto.SimpleNoticeResponseDto;
import com.ajou.travely.service.EventService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/events")
@RestController
public class EventAdminController {

    private final EventService eventService;

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<EventResponseDto> createEvent(@LoginUser SessionUser sessionUser,
        @Valid @ModelAttribute EventCreateRequestDto requestDto) {
        return ResponseEntity.ok(eventService.createEvent(sessionUser.getUserId(), requestDto));
    }

    @GetMapping()
    ResponseEntity<Page<SimpleEventResponseDto>> showAllNotices(@PageableDefault(
            sort = {"id"},
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return ResponseEntity.ok(eventService.getEvents(pageable));
    }

    @GetMapping("/{eventId}")
    ResponseEntity<EventResponseDto> showNotice(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long eventId,
        @RequestBody EventUpdateDto eventUpdateDto) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, eventUpdateDto));
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }

}
