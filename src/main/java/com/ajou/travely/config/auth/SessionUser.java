package com.ajou.travely.config.auth;

import lombok.Getter;

@Getter
public class SessionUser {
    private final Long userId;
    private final String name;
    private final String profilePath;

    public SessionUser(Long userId, String name, String profilePath) {
        this.userId = userId;
        this.name = name;
        this.profilePath = profilePath;
    }
}
