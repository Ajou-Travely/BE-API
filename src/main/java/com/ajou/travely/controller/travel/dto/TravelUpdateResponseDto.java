package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.travel.Travel;
import lombok.Getter;

@Getter
public class TravelUpdateResponseDto {
    private final String title;
    private final String memo;
    private final Integer budget;

    public TravelUpdateResponseDto(Travel entity) {
        this.title = entity.getTitle();
        this.memo = entity.getMemo();
        this.budget = entity.getBudget();
    }
}
