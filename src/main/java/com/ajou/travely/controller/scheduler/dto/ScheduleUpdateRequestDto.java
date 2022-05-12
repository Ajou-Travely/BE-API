package com.ajou.travely.controller.scheduler.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleUpdateRequestDto {
    private final Long scheduleId;
    private final Long placeId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<Long> userIds;

    @Builder
    public ScheduleUpdateRequestDto(Long scheduleId, Long placeId, LocalDateTime startTime, LocalDateTime endTime, List<Long> userIds) {
        this.scheduleId = scheduleId;
        this.placeId = placeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userIds = userIds;
    }
}
