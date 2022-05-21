package com.ajou.travely.service;

import com.ajou.travely.config.auth.CustomAuthentication;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.domain.AuthorizationKakao;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2Service {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${auth.kakaoOauth2ClinetId}")
    private String kakaoOauth2ClinetId;
    @Value("${auth.frontendRedirectUrl}")
    private String frontendRedirectUrl;

    public AuthorizationKakao callTokenApi(String code) {
        String grantType = "authorization_code";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", kakaoOauth2ClinetId);
        params.add("redirect_uri", "https://dev.travely.guide/oauth/kakao/callback");
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        System.out.println("request.getBody() = " + request.getBody());
        String url = "https://kauth.kakao.com/oauth/token";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("response.getBody() = " + response.getBody());

            return objectMapper.readValue(response.getBody(), AuthorizationKakao.class);
        } catch (RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            throw new RestClientException("error");
        }
    }

    public JSONObject callGetUserByAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://kapi.kakao.com/v2/user/me";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            SecurityContext context = SecurityContextHolder.getContext();
            System.out.println("response = " + response.getBody());
            JSONObject userInfo = stringToJson(response.getBody());

            return userInfo;
        }catch (RestClientException | ParseException ex) {
            ex.printStackTrace();
            throw new RestClientException("error");
        }
    }

    public JSONObject setSessionOrRedirectToSignUp(JSONObject userInfoFromKakao) {
        Long kakaoId = (Long) userInfoFromKakao.get("id");
        JSONObject kakao_account = (JSONObject) userInfoFromKakao.get("kakao_account");
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        JSONObject result = new JSONObject();
        if(kakao_account.get("email") != null) {
            if(user.isEmpty()) {
                result.put("status", 301);
                result.put("kakaoId", kakaoId);
                return result;
            } else {
                User exUser = user.get();
                JSONObject token = jwtTokenProvider.createToken(exUser.getId());
                result.put("status", 200);
                result.put("sessionUser", new SessionUser(exUser.getId(), exUser.getName()));
                result.put("token", token.get("token"));
                result.put("expiration", token.get("expiration"));
            }
        } else {
            result.put("status", 401);
        }

        return result;
    }
    public JSONObject stringToJson(String userInfo) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(userInfo);
        return (JSONObject) object;
    }

}
