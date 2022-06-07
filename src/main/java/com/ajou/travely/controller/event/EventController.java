package com.ajou.travely.controller.event;

import com.ajou.travely.controller.event.dto.EventResponseDto;
import com.ajou.travely.controller.event.dto.SimpleEventResponseDto;
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
@RestController
@RequestMapping("/v1/events")
public class EventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @GetMapping("")
    public ResponseEntity<Page<SimpleEventResponseDto>> showEvents(
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SimpleEventResponseDto> events = eventService.getEvents(pageable);
        return ResponseEntity.ok(events);
    }

}
