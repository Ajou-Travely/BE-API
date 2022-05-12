package com.ajou.travely.controller.place.dto;

import com.ajou.travely.domain.Place;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PlaceCreateRequestDto {
    @NotNull
    private Double x;
    @NotNull
    private Double y;
    @NotNull
    private String placeName;
    private String phoneNumber;
    @NotNull
    private String addressName;
    @NotNull
    private String addressRoadName;
    @NotNull
    private String placeUrl;
    @NotNull
    private Long kakaoMapId;

    public Place toEntity() {
        return Place.builder()
                .x(this.x)
                .y(this.y)
                .placeName(this.placeName)
                .phoneNumber(this.phoneNumber)
                .addressName(this.addressName)
                .addressRoadName(this.addressRoadName)
                .placeUrl(this.placeUrl)
                .kakaoMapId(this.kakaoMapId)
                .build();
    }

    @Builder
    public PlaceCreateRequestDto(Double x, Double y, String placeName, String phoneNumber, String addressName, String addressRoadName, String placeUrl, Long kakaoMapId) {
        this.x = x;
        this.y = y;
        this.placeName = placeName;
        this.phoneNumber = phoneNumber;
        this.addressName = addressName;
        this.addressRoadName = addressRoadName;
        this.placeUrl = placeUrl;
        this.kakaoMapId = kakaoMapId;
    }
}
