package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.travel.TravelDate;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TravelDateCreateResponseDto {
    private String title;
    private LocalDate date;

    public TravelDateCreateResponseDto(TravelDate entity) {
        this.title = entity.getTitle();
        this.date = entity.getDate();
    }
}
