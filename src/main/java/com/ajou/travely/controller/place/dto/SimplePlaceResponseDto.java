package com.ajou.travely.controller.place.dto;

import com.ajou.travely.domain.Place;
import lombok.Getter;

@Getter
public class SimplePlaceResponseDto {
    private final Long placeId;
    private final String placeName;
    private final Double lat;
    private final Double lng;

    public SimplePlaceResponseDto(Place entity) {
        this.placeId = entity.getId();
        this.placeName = entity.getPlaceName();
        this.lat =entity.getLat();
        this.lng = entity.getLng();
    }
}
