package com.ajou.travely.config.auth;

import lombok.Getter;

@Getter
public class SessionUser {
    private Long userId;

    public SessionUser(Long userId) {
        this.userId = userId;
    }
}
