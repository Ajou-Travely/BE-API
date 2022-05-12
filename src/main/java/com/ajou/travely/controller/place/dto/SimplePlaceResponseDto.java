package com.ajou.travely.controller.place.dto;

import com.ajou.travely.domain.Place;
import lombok.Getter;

@Getter
public class SimplePlaceResponseDto {
    private Long placeId;
    private String placeName;

    public SimplePlaceResponseDto(Place entity) {
        this.placeId = entity.getId();
        this.placeName = entity.getPlaceName();
    }
}
