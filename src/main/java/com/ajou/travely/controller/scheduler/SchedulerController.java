package com.ajou.travely.controller.scheduler;

import com.ajou.travely.controller.scheduler.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.scheduler.dto.ScheduleResponseDto;
import com.ajou.travely.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SchedulerController {
    private final ScheduleService scheduleService;

    public SchedulerController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/api/v1/schedules/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }

    @PostMapping("/api/v1/schedules")
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        return ResponseEntity.ok(scheduleService.createSchedule(scheduleCreateRequestDto));
    }
}
