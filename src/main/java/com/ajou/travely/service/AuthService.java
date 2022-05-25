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

import java.util.Map;
import java.util.Optional;

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
        JSONObject result = new JSONObject();

        Optional<User> res = userRepository.findByEmail(emailPasswordInputDto.getEmail());
        if (res.isPresent()) {
            User user = res.get();
            if (!user.getPassword().equals(emailPasswordInputDto.getPassword())) {
                result.put("status", 400);
                result.put("message", "비밀번호가 틀렸습니다.");

                return result;
            }
            String token = jwtTokenProvider.createToken(user.getId());
            result.put("status", 200);
            result.put("token", token);

            return result;
        }
        result.put("status", 400);
        result.put("message", "해당 이메일을 가진 사용자를 찾을 수 없습니다.");

        return result;
    }
}
