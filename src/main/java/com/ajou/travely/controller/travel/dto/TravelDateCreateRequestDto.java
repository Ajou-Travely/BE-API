package com.ajou.travely.controller.travel.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TravelDateCreateRequestDto {
    private String title;
    private LocalDate date;
}
