package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ScheduleCreateRequestDto {
    @NotNull
    private final PlaceCreateRequestDto place;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private final LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private final LocalDateTime endTime;
    @NotNull
    private final List<Long> userIds;
    @NotNull
    private final LocalDate date;

    @Builder
    public ScheduleCreateRequestDto(@NonNull PlaceCreateRequestDto place, @NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime, List<Long> userIds, @NonNull LocalDate date) {
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userIds = userIds != null ? userIds : new ArrayList<>();
        this.date = date;
    }
}
