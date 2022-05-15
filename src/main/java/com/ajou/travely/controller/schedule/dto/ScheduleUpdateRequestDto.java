package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.place.dto.SimplePlaceResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleUpdateRequestDto {
    private final PlaceCreateRequestDto place;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<Long> userIds;

    @Builder
    public ScheduleUpdateRequestDto(Long scheduleId, PlaceCreateRequestDto place, LocalDateTime startTime, LocalDateTime endTime, List<Long> userIds) {
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userIds = userIds;
    }
}
