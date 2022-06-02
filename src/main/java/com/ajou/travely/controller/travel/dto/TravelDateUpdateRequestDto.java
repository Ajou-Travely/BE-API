package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TravelDateUpdateRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public TravelDateUpdateRequestDto(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
