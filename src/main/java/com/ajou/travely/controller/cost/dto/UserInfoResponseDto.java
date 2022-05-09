package com.ajou.travely.controller.cost.dto;

import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private final Long userId;
    private final String userName;

    public UserInfoResponseDto(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
