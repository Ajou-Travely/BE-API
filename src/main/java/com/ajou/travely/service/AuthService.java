package com.ajou.travely.service;

import com.ajou.travely.controller.auth.dto.EmailPasswordInputDto;
import com.ajou.travely.domain.AuthorizationKakao;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Oauth2Service oauth2Service;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public JSONObject kakaoAuthentication(String origin, String code) {
        AuthorizationKakao authorizationKakao = oauth2Service.callTokenApi(origin, code);
        JSONObject userInfoFromKakao = oauth2Service.callGetUserByAccessToken(authorizationKakao.getAccess_token());
        return oauth2Service.setSessionOrRedirectToSignUp(userInfoFromKakao);
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getDetails();
        return user.getId();
    }

    public JSONObject login(EmailPasswordInputDto emailPasswordInputDto) {
        User user = userRepository.findByEmail(emailPasswordInputDto.getEmail())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 이메일을 가진 사용자를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        if (user.getPassword() != emailPasswordInputDto.getPassword()) {
            throw new RecordNotFoundException(
                    "비밀번호가 틀렸습니다.",
                    ErrorCode.INVALID_PASSWORD
            );
        }

        String token = jwtTokenProvider.createToken(user.getId());
        JSONObject result = new JSONObject();
        result.put("status", 200);
        result.put("token", token);

        return result;
    }
}
