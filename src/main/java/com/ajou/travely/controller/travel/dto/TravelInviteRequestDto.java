package com.ajou.travely.controller.travel.dto;

import lombok.Getter;

@Getter
public class TravelInviteRequestDto {
    private final String email;

    public TravelInviteRequestDto(String email) {
        this.email = email;
    }
}
