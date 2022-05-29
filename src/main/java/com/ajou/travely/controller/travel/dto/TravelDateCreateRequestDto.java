package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TravelDateCreateRequestDto {
    private String title;
    @NotNull(message = "날짜가 필요합니다.")
    private LocalDate date;

    @Builder
    public TravelDateCreateRequestDto(String title, @NonNull LocalDate date) {
        this.title = title;
        this.date = date;
    }
}
