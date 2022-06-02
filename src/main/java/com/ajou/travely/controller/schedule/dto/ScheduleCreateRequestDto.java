package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.domain.travel.TravelDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ScheduleCreateRequestDto {
    @NotNull
    private final PlaceCreateRequestDto place;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private final LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private final LocalTime endTime;
    @NotNull
    private final List<Long> userIds;

    @Builder
    public ScheduleCreateRequestDto(@NonNull PlaceCreateRequestDto place, @NonNull LocalTime startTime, @NonNull LocalTime endTime, List<Long> userIds) {
        this.place = place;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userIds = userIds != null ? userIds : new ArrayList<>();
    }
}
