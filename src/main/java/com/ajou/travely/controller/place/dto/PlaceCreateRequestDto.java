package com.ajou.travely.controller.place.dto;

import com.ajou.travely.domain.Place;
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

    public Place toEntity() {
        return Place.builder()
                .x(this.x)
                .y(this.y)
                .placeName(this.placeName)
                .phoneNumber(this.phoneNumber)
                .addressName(this.addressName)
                .addressRoadName(this.addressRoadName)
                .placeUrl(this.placeUrl)
                .build();
    }
}
