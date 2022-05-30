package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TravelDateTitleUpdateRequestDto {
    private LocalDate date;
    private String title;

    @Builder
    public TravelDateTitleUpdateRequestDto(LocalDate date, String title) {
        this.date = date;
        this.title = title;
    }
}
