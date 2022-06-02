package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
public class TravelUpdateRequestDto {
    private final String title;
//    private final LocalDate startDate;
//    private final LocalDate endDate;
    private final String memo;
    private final Integer budget;

    @Builder
    public TravelUpdateRequestDto(String title,
//                                  LocalDate startDate,
//                                  LocalDate endDate,
                                  String memo,
                                  Integer budget) {
        this.title = title;
//        this.startDate = startDate;
//        this.endDate = endDate;
        this.memo = memo;
        this.budget = budget;
    }
}
