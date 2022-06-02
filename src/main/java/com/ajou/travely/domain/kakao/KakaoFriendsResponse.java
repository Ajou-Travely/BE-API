package com.ajou.travely.domain.kakao;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoFriendsResponse {
    private final KakaoFriend[] elements;
    private final Integer total_count;
    private final String before_url;
    private final String after_url;
    private final Integer favorite_count;

    @Builder
    public KakaoFriendsResponse(KakaoFriend[] elements, Integer total_count, String before_url, String after_url, Integer favorite_count) {
        this.elements = elements;
        this.total_count = total_count;
        this.before_url = before_url;
        this.after_url = after_url;
        this.favorite_count = favorite_count;
    }
}
