package com.ajou.travely.controller.scheduler.dto;

import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long id;
    private Place place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ScheduleResponseDto(Schedule entity) {
        this.id = entity.getId();
        this.place = entity.getPlace();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
    }
}
