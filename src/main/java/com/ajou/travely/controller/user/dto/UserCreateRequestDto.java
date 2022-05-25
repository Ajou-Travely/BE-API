package com.ajou.travely.controller.user.dto;

import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.domain.user.Sex;
import com.ajou.travely.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserCreateRequestDto {
    @NotNull(message = "이름이 필요합니다.")
    private String name;

    @NotNull(message = "이메일이 필요합니다.")
    private String email;

    private String password;
//
//    private Sex sex;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate birthday;
    private Sex sex;

    @NotNull(message = "전화번호가 필요합니다.")
    private String phoneNumber;

//    @NotNull(message = "카카오 아이디가 필요합니다.")
    private Long kakaoId;

    @Builder
    public UserCreateRequestDto(@NonNull String name,
                                @NonNull String email,
//                                Sex sex,
//                                LocalDate birthday,
                                @NonNull String phoneNumber,
                                Long kakaoId,
                                String password) {
        this.name = name;
        this.email = email;
//        this.sex = sex;
//        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.kakaoId = kakaoId;
        this.password = password;
    }

    public User toEntity() {
        return User.builder()
            .userType(UserType.USER)
            .name(this.name)
            .email(this.email)
            .password(this.password)
            .phoneNumber(this.phoneNumber)
            .kakaoId(this.kakaoId)
            .build();
    }
}
