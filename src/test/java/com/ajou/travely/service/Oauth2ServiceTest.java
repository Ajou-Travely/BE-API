package com.ajou.travely.service;

import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Oauth2ServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Oauth2Service oauth2Service;

    @Test
    void testWhetherUserExists() {
        User user = new User();
        Long kakaoId = 123456789L;
        user.setType(Type.USER);
        user.setEmail("test@email.com");
        user.setName("테스트");
        user.setPhoneNumber("01001000100");
        user.setSex("MAN");
        user.setKakaoId(kakaoId);
        userRepository.save(user);

        String s1 = oauth2Service.setSessionOrRedirectToSignUp(kakaoId);
        String s2 = oauth2Service.setSessionOrRedirectToSignUp(123L);

        assertThat(s1).isEqualTo("세션 저장 완료");
        assertThat(s2).isEqualTo("회원가입이 필요한 상태");
    }
}