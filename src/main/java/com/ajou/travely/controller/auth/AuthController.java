package com.ajou.travely.controller.auth;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.auth.dto.EmailPasswordInputDto;
import com.ajou.travely.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @PostMapping("/v1/login")
    public ResponseEntity<JSONObject> login(@Valid @RequestBody EmailPasswordInputDto emailPasswordInputDto) {
        JSONObject result = authService.login(emailPasswordInputDto);
        return ResponseEntity.ok(result);
    }
}
