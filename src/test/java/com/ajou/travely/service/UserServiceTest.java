package com.ajou.travely.service;

import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
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

    @Test
    void testGetUserInfo() {
        String email = "test@test";
        String phoneNumber = "119";
        String name = "119";
        LocalDate birthday = LocalDate.of(1998, 6, 3);
        User user = userService.insertUser(
                User.builder().kakaoId(0L)
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
}