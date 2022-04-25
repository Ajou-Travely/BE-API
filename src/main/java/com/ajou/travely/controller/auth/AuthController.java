package com.ajou.travely.controller.auth;

import com.ajou.travely.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/oauth2/authorization/kakao")
    public String login(@RequestParam("code") String code) {
        return authService.kakaoAuthentication(code);
    }
}
