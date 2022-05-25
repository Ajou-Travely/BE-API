package com.ajou.travely.service;

import com.ajou.travely.controller.user.dto.UserUpdateRequestDto;
import com.ajou.travely.domain.user.Mbti;
import com.ajou.travely.domain.user.Sex;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
        "spring.mail.password=temptemptemptemp"

})
class UserServiceTest {
    @Autowired
    UserService userService;

//    EntityManager

    @Test
    void testGetUserInfo() {
        String email = "test@test";
        String phoneNumber = "119";
        String name = "119";
        LocalDate birthday = LocalDate.of(1998, 6, 3);
        User user = userService.insertUser(
                User.builder()
                    .kakaoId(0L)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .name(name)
                    .userType(UserType.USER)
                    .birthday(birthday)
                    .build()
        );

        User findUser = userService.findUserById(user.getId());

        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void testUpdateUser() {
        // given
        String email = "test@test";
        String phoneNumber = "119";
        String name = "119";
        LocalDate birthday = LocalDate.of(1998, 6, 3);
        User user = userService.insertUser(
            User.builder()
                .userType(UserType.USER)
                .phoneNumber(phoneNumber)
                .email(email)
                .name(name)
                .kakaoId(0L)
                .build()
        );

        // when
        String updateName = "update name";
        String updatePhoneNumber = "010-1111-1111";
        LocalDate updateBirthday = LocalDate.of(2020, 6, 3);
        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
            .name(updateName)
            .phoneNumber(updatePhoneNumber)
            .mbti(Mbti.ENFP)
            .sex(Sex.FEMALE)
            .birthday(updateBirthday)
            .build();

        userService.updateUser(user.getId(), requestDto);

        // then
        User updateUser = userService.findUserById(user.getId());
        System.out.println(updateUser.toString());
    }

}