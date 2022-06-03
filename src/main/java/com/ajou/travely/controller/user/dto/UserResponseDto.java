package com.ajou.travely.controller.user.dto;

import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long userId;

    private final String name;

    private final String email;

//    private final Sex sex;

    private final String phoneNumber;

//    private final LocalDate birthday;

    private final String profilePath;

    private final UserType userType;

    public UserResponseDto(User entity) {
        this.userId = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
//        this.sex = entity.getSex();
        this.phoneNumber = entity.getPhoneNumber();
        this.profilePath = entity.getProfilePath();
        this.userType = entity.getUserType();
//        this.birthday = entity.getBirthday();
    }
}
