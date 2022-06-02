package com.ajou.travely.domain.kakao;

import lombok.Getter;

@Getter
public class KakaoFriend {
    private final Long id;
    private final String uuid;
    private final Boolean favorite;
    private final String profile_nickname;
    private final String profile_thumbnail_image;

    public KakaoFriend(Long id, String uuid, Boolean favorite, String profile_nickname, String profile_thumbnail_image) {
        this.id = id;
        this.uuid = uuid;
        this.favorite = favorite;
        this.profile_nickname = profile_nickname;
        this.profile_thumbnail_image = profile_thumbnail_image;
    }
}
