package com.ajou.travely.controller.user.dto;

import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SimpleUserInfoDto {
    private final Long userId;
    private final String userName;
    private final String email;
    private final String profilePath;

    public SimpleUserInfoDto(User entity) {
        this.userId = entity.getId();
        this.userName = entity.getName();
        this.email = entity.getEmail();
        this.profilePath = entity.getProfilePath();
    }
}
