package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
public class TravelUpdateRequestDto {
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String memo;

    @Builder
    public TravelUpdateRequestDto(@NonNull String title,
                                  @NonNull LocalDate startDate,
                                  @NonNull LocalDate endDate,
                                  @NonNull String memo) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memo = memo;
    }
}
