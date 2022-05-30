package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
public class ScheduleUpdateRequestDto {
    private final PlaceCreateRequestDto place;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final List<Long> userIds;

    @Builder
    public ScheduleUpdateRequestDto(Long scheduleId, PlaceCreateRequestDto place, LocalTime startTime, LocalTime endTime, List<Long> userIds) {
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userIds = userIds;
    }
}
