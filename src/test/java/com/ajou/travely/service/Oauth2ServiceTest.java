package com.ajou.travely.service;

import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.transaction.Transactional;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
        "spring.mail.password=temptemptemptemp"
})
@Transactional
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
    @Rollback
    void testWhetherUserExists() {
        JSONObject userInfoFromKakao = new JSONObject();
        JSONObject kakao_account = new JSONObject();
        kakao_account.put("email", "test@test.com");
        userInfoFromKakao.put("id", 123456789L);
        userInfoFromKakao.put("kakao_account", kakao_account);
        Long kakaoId = (Long) userInfoFromKakao.get("id");

        JSONObject notSignedUpUserInfo = new JSONObject();
        notSignedUpUserInfo.put("id", 123L);
        notSignedUpUserInfo.put("kakao_account", kakao_account);

        JSONObject userInfoWithoutEmail = new JSONObject();
        JSONObject kakao_accountWithoutEmail = new JSONObject();
        userInfoWithoutEmail.put("id", 1234L);
        userInfoWithoutEmail.put("kakao_account", kakao_accountWithoutEmail);


        User user = User.builder()
                .type(Type.USER)
                .email("test@email.com")
                .name("NAME")
                .phoneNumber("0101010101010")
                .kakaoId(kakaoId)
                .build();

        userRepository.save(user);

        JSONObject s1 = oauth2Service.setSessionOrRedirectToSignUp(userInfoFromKakao);
        //User kakaoIdInAuthenticationDetail = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();
        JSONObject s2 = oauth2Service.setSessionOrRedirectToSignUp(notSignedUpUserInfo);
        JSONObject s3 = oauth2Service.setSessionOrRedirectToSignUp(userInfoWithoutEmail);

//        System.out.println("SecurityContextHolder.getContext().getAuthentication().getDetails() = " + SecurityContextHolder.getContext().getAuthentication().getDetails());
//        SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().forEach(grantedAuthority -> {
//            System.out.println("grantedAuthority.getAuthority().toString() = " + grantedAuthority.getAuthority().toString());
//        });
        assertThat(s1.get("status")).isEqualTo(200);
        assertThat(s1.get("token")).isNotNull();
        assertThat(s2.get("status")).isEqualTo(301);
        assertThat(s2.get("token")).isNull();
        assertThat(s3.get("status")).isEqualTo(401);
//        assertThat(kakaoIdInAuthenticationDetail.getKakaoId()).isEqualTo(kakaoId);
    }
}