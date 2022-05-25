package com.ajou.travely.service;

import com.ajou.travely.controller.auth.dto.EmailPasswordInputDto;
import com.ajou.travely.domain.user.CustomUserDetails;
import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
        "spring.mail.password=temptemptemptemp"
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
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("카카오 로그인을 통한 Jwt 토큰 생성하기")
    void getUserId() {
        JSONObject userInfoFromKakao = new JSONObject();
        JSONObject kakao_account = new JSONObject();
        kakao_account.put("email", "test@test.com");
        userInfoFromKakao.put("id", 123456789L);
        userInfoFromKakao.put("kakao_account", kakao_account);
        Long kakaoId = (Long) userInfoFromKakao.get("id");
        User user = User.builder()
                .userType(UserType.USER)
                .email("test@email.com")
                .name("NAME")
                .phoneNumber("0101010101010")
                .kakaoId(kakaoId)
                .build();

        userRepository.save(user);
        JSONObject result = oauth2Service.setSessionOrRedirectToSignUp(userInfoFromKakao);
//        Long userId = authService.getUserId();

        assertThat(result.get("status")).isEqualTo(200);
        assertThat(result.get("token")).isNotNull();
    }

    @Test
    @DisplayName("이메일과 비밀번호를 통해 Jwt 토큰 생성하기")
    void testLogin() throws ParseException {
        String email = "test@test.com";
        String password = "password";

        User user = userRepository.save(User.builder()
                .userType(UserType.USER)
                .email(email)
                .name("test")
                .phoneNumber("01011112222")
                .password(password)
                .build());

        String token = authService.login(new EmailPasswordInputDto(email, password));
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) jwtTokenProvider.getAuthentication(token);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(principal.getUser().getId()).isEqualTo(user.getId());
        Assertions.assertThatThrownBy(() -> {
            authService.login(new EmailPasswordInputDto("wrong@email.com", password));
        }).isInstanceOf(RecordNotFoundException.class).hasMessageContaining("이메일");
        Assertions.assertThatThrownBy(() -> {
            authService.login(new EmailPasswordInputDto(email, "invalid_password"));
        }).isInstanceOf(RecordNotFoundException.class).hasMessageContaining("비밀번호");
    }
}