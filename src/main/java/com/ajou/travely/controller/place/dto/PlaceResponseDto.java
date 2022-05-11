package com.ajou.travely.controller.place.dto;

import com.ajou.travely.domain.Place;
import lombok.Getter;

@Getter
public class PlaceResponseDto {
    private Long placeId;
    private Double x;
    private Double y;
    private String placeName;
    private String phoneNumber;
    private String addressName;
    private String addressRoadName;
    private String placeUrl;

    public PlaceResponseDto(Place entity) {
        this.placeId = entity.getId();
        this.x = entity.getX();
        this.y = entity.getY();
        this.placeName = entity.getPlaceName();
        this.phoneNumber = entity.getPhoneNumber();
        this.addressName = entity.getAddressName();
        this.addressRoadName = entity.getAddressRoadName();
        this.placeUrl = entity.getPlaceUrl();
    }
}
