package com.ajou.travely.controller.auth;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/v1/oauth2/authorization/kakao")
    public JSONObject login(@RequestParam("code") String code) {
        return authService.kakaoAuthentication(code);
    }

    @GetMapping("/v1/isLogin")
    public Boolean isLogin(@RequestHeader("Cookie") Optional<Object> header) {
        return header.isPresent();
    }
}
