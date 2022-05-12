package com.ajou.travely.service;

import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserRepository;
import javax.transaction.Transactional;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
})
@Transactional
class AuthServiceTest {
    @Value("${auth.kakaoOauth2ClinetId}")
    public String kakaoOauth2ClientId;
    @Value("${auth.frontendRedirectUrl}")
    public String frontendRedirectUrl;
    @Autowired
    private Oauth2Service oauth2Service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("Security Context에서 사용자 id 가져오기")
    @Rollback
    void getUserId() {
        JSONObject userInfoFromKakao = new JSONObject();
        JSONObject kakao_account = new JSONObject();
        kakao_account.put("email", "test@test.com");
        userInfoFromKakao.put("id", 123456789L);
        userInfoFromKakao.put("kakao_account", kakao_account);
        Long kakaoId = (Long) userInfoFromKakao.get("id");
        User user = User.builder()
                .type(Type.USER)
                .email("test@email.com")
                .name("NAME")
                .phoneNumber("0101010101010")
                .kakaoId(kakaoId)
                .build();

        userRepository.save(user);
        JSONObject result = oauth2Service.setSessionOrRedirectToSignUp(userInfoFromKakao);
        Long userId = authService.getUserId();

        assertThat(result.get("status")).isEqualTo(200);
        assertThat(userId).isEqualTo(user.getId());

    }
}