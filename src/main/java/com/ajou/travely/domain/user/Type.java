package com.ajou.travely.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    GUEST("ROLE_GUEST", "guest"),
    USER("ROLE_USER", "user"),
    ADMIN("ROLE_ADMIN", "administrator"),
    HOST("ROLE_HOST", "host"),
    LOCAL_GOVERNMENT("ROLE_LOCAL_GOVERNMENT", "local government")
    ;


    private final String key;
    private final String title;
}
