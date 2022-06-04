package com.ajou.travely.controller.admin;

import com.ajou.travely.controller.event.dto.EventResponseDto;
import com.ajou.travely.controller.event.dto.SimpleEventResponseDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.controller.notice.dto.SimpleNoticeResponseDto;
import com.ajou.travely.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/events")
@RestController
public class EventAdminController {

    private final EventService eventService;

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
}
