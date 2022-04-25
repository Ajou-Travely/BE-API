package com.ajou.travely.controller.travel.dto.user;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SimpleUserInfoDto {
    private final Long userId;
    private final String userName;

    public SimpleUserInfoDto(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
