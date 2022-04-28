package com.ajou.travely.controller.user.dto;

import com.ajou.travely.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class UserResponseInfoDto {
    private final Long userId;
    private final String userType;
    private final String name;
    private final String email;
    private final String phoneNumber;

    public UserResponseInfoDto(User entity) {
        this.userId = entity.getId();
        this.userType = entity.getType().toString();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
    }
}
