package com.ajou.travely.service;

import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        properties = {
            "auth.kakaoOauth2ClinetId=test",
                "auth.frontendRedirectUrl=test",
        }
)
class Oauth2ServiceTest {
    @Value("${auth.kakaoOauth2ClinetId}")
    public String kakaoOauth2ClientId;
    @Value("${auth.frontendRedirectUrl}")
    public String frontendRedirectUrl;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Oauth2Service oauth2Service;

    @Test
    @DisplayName("카카오 유저 아이디를 통해 유저를 찾고 회원가입 여부 반환")
    void testWhetherUserExists() {
        Long kakaoId = 123456789L;
        User user = User.builder()
                .type(Type.USER)
                .email("test@email.com")
                .name("NAME")
                .phoneNumber("0101010101010")
                .kakaoId(kakaoId)
                .build();

        userRepository.save(user);

        String s1 = oauth2Service.setSessionOrRedirectToSignUp(kakaoId);
        String s2 = oauth2Service.setSessionOrRedirectToSignUp(123L);

        assertThat(s1).isEqualTo("세션 저장 완료");
        assertThat(s2).isEqualTo("회원가입이 필요한 상태");
    }
}