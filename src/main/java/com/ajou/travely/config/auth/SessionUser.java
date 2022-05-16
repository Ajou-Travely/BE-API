package com.ajou.travely.config.auth;

import lombok.Getter;

@Getter
public class SessionUser {
    private Long userId;
    private String name;

    public SessionUser(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
