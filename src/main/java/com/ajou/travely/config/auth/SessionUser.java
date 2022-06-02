package com.ajou.travely.config.auth;

import lombok.Getter;

@Getter
public class SessionUser {
    private final Long userId;
    private final String name;
    private final String profilePath;
    private final String accessToken;

    public SessionUser(Long userId, String name, String profilePath, String accessToken) {
        this.userId = userId;
        this.name = name;
        this.profilePath = profilePath;
        this.accessToken = accessToken;
    }
}
