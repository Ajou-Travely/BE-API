package com.ajou.travely.controller.auth.dto;

import lombok.Getter;

@Getter
public class EmailPasswordInputDto {
    private String email;
    private String password;

    public EmailPasswordInputDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
