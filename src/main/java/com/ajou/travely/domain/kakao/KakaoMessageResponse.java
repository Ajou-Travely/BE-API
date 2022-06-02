package com.ajou.travely.domain.kakao;

public class KakaoMessageResponse {
    private final String[] successful_receiver_uuids;
    private final KakaoMessageFailureInfo[] failure_info;

    public KakaoMessageResponse(String[] successful_receiver_uuids, KakaoMessageFailureInfo[] failure_info) {
        this.successful_receiver_uuids = successful_receiver_uuids;
        this.failure_info = failure_info;
    }
}
