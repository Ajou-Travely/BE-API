package com.ajou.travely.service;

import com.ajou.travely.domain.AuthorizationKakao;
import com.ajou.travely.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Oauth2Service oauth2Service;

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
}
