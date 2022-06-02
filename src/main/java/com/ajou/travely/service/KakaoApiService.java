package com.ajou.travely.service;

import com.ajou.travely.domain.kakao.KakaoFriendsResponse;
import com.ajou.travely.domain.kakao.KakaoMessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoApiService {

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public KakaoApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public KakaoMessageResponse sendTextMessage(String[] receiverUuids, String templateObject, String accessToken) {
        String url = "https://kapi.kakao.com/v1/api/talk/friends/message/default/send";
        HttpHeaders headers = new HttpHeaders();
        String token = "Bearer " + accessToken;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", token);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("receiver_uuids", receiverUuids);
        params.add("template_object", templateObject);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return objectMapper.readValue(response.getBody(), KakaoMessageResponse.class);
        } catch(RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            throw new RestClientException("error");
        }
    }

    public KakaoFriendsResponse getKakaoFriends(String accessToken) {
        String url = "https://kapi.kakao.com/v1/api/talk/friends";
        HttpHeaders headers = new HttpHeaders();
        String token = "Bearer " + accessToken;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", token);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return objectMapper.readValue(response.getBody(), KakaoFriendsResponse.class);
        } catch(RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            throw new RestClientException("error");
        }
    }

}
