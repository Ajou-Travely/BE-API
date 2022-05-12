package com.ajou.travely.controller.schedule;

import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/schedules")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @PostMapping("")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(scheduleCreateRequestDto));
    }
}
