package com.ajou.travely.controller.travel.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TravelDateCreateRequestDto {
    private String title;
    @NotNull(message = "날짜가 필요합니다.")
    private LocalDate date;
}
