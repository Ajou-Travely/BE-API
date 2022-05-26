package com.ajou.travely.controller.auth.dto;

import lombok.Getter;

@Getter
public class LoginSuccessResponseDto {
    private String token;

    public LoginSuccessResponseDto(String token) {
        this.token = token;
    }
}
