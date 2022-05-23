package com.ajou.travely.controller.place.dto;

import com.ajou.travely.domain.Place;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PlaceCreateRequestDto {
    @NotNull
    private final Double lat;
    @NotNull
    private final Double lng;
    @NotNull
    private final String placeName;
    private final String phoneNumber;
    @NotNull
    private final String addressName;
    @NotNull
    private final String addressRoadName;
    @NotNull
    private final String placeUrl;
    @NotNull
    private final Long kakaoMapId;

    public Place toEntity() {
        return Place.builder()
                .lat(this.lat)
                .lng(this.lng)
                .placeName(this.placeName)
                .phoneNumber(this.phoneNumber)
                .addressName(this.addressName)
                .addressRoadName(this.addressRoadName)
                .placeUrl(this.placeUrl)
                .kakaoMapId(this.kakaoMapId)
                .build();
    }

    @Builder
    public PlaceCreateRequestDto(Double lat, Double lng, String placeName, String phoneNumber, String addressName, String addressRoadName, String placeUrl, Long kakaoMapId) {
        this.lat = lat;
        this.lng = lng;
        this.placeName = placeName;
        this.phoneNumber = phoneNumber;
        this.addressName = addressName;
        this.addressRoadName = addressRoadName;
        this.placeUrl = placeUrl;
        this.kakaoMapId = kakaoMapId;
    }
}
