package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.travel.TravelDate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TravelDateResponseDto {
    private final String title;
    private final LocalDate date;

    @Builder
    public TravelDateResponseDto(TravelDate entity) {
        this.title = entity.getTitle();
        this.date = entity.getDate();
    }
}
