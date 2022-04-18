package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.Travel;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class TravelListResponseDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String memo;

    public TravelListResponseDto(Travel entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.memo = entity.getMemo();
    }
}
