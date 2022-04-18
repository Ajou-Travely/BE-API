package com.ajou.travely.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Place {
    @Id
    @GeneratedValue
    @Column(name = "place_id")
    private Long id;

    private Double x;

    private Double y;

    private String placeName;

    private String phoneNumber;

    private String addressName;

    private String addressRoadName;

    private String placeUrl;
}
