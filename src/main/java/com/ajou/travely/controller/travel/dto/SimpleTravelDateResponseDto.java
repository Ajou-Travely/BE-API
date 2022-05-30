package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.travel.TravelDate;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleTravelDateResponseDto {
    private Long travelId;
    private LocalDate date;
    private String title;
    private List<SimpleScheduleResponseDto> schedules;

    public SimpleTravelDateResponseDto(TravelDate entity) {
        this.travelId = entity.getTravel().getId();
        this.title = entity.getTitle();
        this.date = entity.getDate();;
        this.schedules = entity
                .getSchedules()
                .stream()
                .map(SimpleScheduleResponseDto::new)
                .collect(Collectors.toList());
    }
}
