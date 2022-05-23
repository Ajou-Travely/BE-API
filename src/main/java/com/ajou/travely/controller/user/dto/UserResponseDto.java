package com.ajou.travely.controller.user.dto;

import com.ajou.travely.domain.user.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {
    private final Long userId;
    private final String name;
    private final String email;
    private final String sex;
    private final String phoneNumber;
    private final LocalDate birthday;

    public UserResponseDto(User entity) {
        this.userId = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.sex = entity.getSex();
        this.phoneNumber = entity.getPhoneNumber();
        this.birthday = entity.getBirthday();
    }
}
