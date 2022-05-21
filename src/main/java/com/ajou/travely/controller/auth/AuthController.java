package com.ajou.travely.controller.auth;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/v1/oauth2/authorization/kakao")
    public JSONObject login(HttpServletRequest request, @RequestParam("code") String code) {
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        return authService.kakaoAuthentication(origin, code);
    }
}
