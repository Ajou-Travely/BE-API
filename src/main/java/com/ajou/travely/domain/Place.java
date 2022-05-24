package com.ajou.travely.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private Double lat;

    private Double lng;

    private String placeName;

    private String phoneNumber;

    private String addressName;

    private String addressRoadName;

    private String placeUrl;
    @Column(unique = true)
    private Long kakaoMapId;

    @Builder
    public Place(@NonNull Double lat, @NonNull Double lng, @NonNull String placeName, String phoneNumber, @NonNull String addressName, @NonNull String addressRoadName, @NonNull String placeUrl, @NonNull Long kakaoMapId) {
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
