package com.ajou.travely.controller.schedule.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ScheduleCreateRequestDto {
    @NotNull
    private Long travelId;
    @NotNull
    private Long placeId;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
    @NotNull
    private List<Long> userIds;

    @Builder
    public ScheduleCreateRequestDto(@NonNull Long travelId, @NonNull Long placeId, @NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime, List<Long> userIds) {
        this.travelId = travelId;
        this.placeId = placeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userIds = userIds != null ? userIds : new ArrayList<>();
    }
}
