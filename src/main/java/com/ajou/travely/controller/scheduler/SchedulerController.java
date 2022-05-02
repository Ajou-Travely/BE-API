package com.ajou.travely.controller.scheduler;

import com.ajou.travely.controller.scheduler.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.scheduler.dto.ScheduleResponseDto;
import com.ajou.travely.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class SchedulerController {
    private final ScheduleService scheduleService;

    public SchedulerController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/api/v1/schedules/{scheduleId}")
    public ScheduleResponseDto getScheduleById(@PathVariable Long scheduleId) {
        return new ScheduleResponseDto(scheduleService.getScheduleWithPlaceById(scheduleId));
    }

    @PostMapping("/api/v1/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleCreateRequestDto scheduleCreateRequestDto) {
        Long travelId = scheduleCreateRequestDto.getTravelId();
        Long placeId = scheduleCreateRequestDto.getPlaceId();
        LocalDateTime startTime = scheduleCreateRequestDto.getStartTime();
        LocalDateTime endTime = scheduleCreateRequestDto.getEndTime();
        return new ScheduleResponseDto(scheduleService.createSchedule(travelId, placeId, startTime, endTime));
    }
}
