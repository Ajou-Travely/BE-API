package com.ajou.travely.controller.travel.dto.user;

import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

@Getter
    public class UserCreateRequestDto {
        private String userType;
        private String name;
        private String email;
        private String phoneNumber;
        private Long kakaoId;

    public User toEntity() {
        return User.builder().type(Type.valueOf(this.userType)).name(this.name).email(this.email).phoneNumber(this.phoneNumber).kakaoId(this.kakaoId).build();
    }
}
