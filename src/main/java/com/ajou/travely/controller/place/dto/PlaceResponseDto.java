package com.ajou.travely.controller.place.dto;

import com.ajou.travely.domain.Place;
import lombok.Getter;

@Getter
public class PlaceResponseDto {
    private final Long placeId;
    private final Double x;
    private final Double y;
    private final String placeName;
    private final String phoneNumber;
    private final String addressName;
    private final String addressRoadName;
    private final String placeUrl;
    private final Long kakaoMapId;

    public PlaceResponseDto(Place entity) {
        this.placeId = entity.getId();
        this.x = entity.getX();
        this.y = entity.getY();
        this.placeName = entity.getPlaceName();
        this.phoneNumber = entity.getPhoneNumber();
        this.addressName = entity.getAddressName();
        this.addressRoadName = entity.getAddressRoadName();
        this.placeUrl = entity.getPlaceUrl();
        this.kakaoMapId = entity.getKakaoMapId();
    }
}
