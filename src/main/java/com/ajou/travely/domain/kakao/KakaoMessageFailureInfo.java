package com.ajou.travely.domain.kakao;

import lombok.Builder;

public class KakaoMessageFailureInfo {
    private final Integer code;
    private final String msg;
    private final String[] receiver_uuids;

    @Builder
    public KakaoMessageFailureInfo(Integer code, String msg, String[] receiver_uuids) {
        this.code = code;
        this.msg = msg;
        this.receiver_uuids = receiver_uuids;
    }
}
