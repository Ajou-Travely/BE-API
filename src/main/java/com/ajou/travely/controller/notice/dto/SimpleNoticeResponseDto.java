package com.ajou.travely.controller.notice.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleNoticeResponseDto {
    private final Long noticeId;
    private final String title;
    private final String content;
    private final String authorName;
    private final String thumbnail;

    @Builder
    public SimpleNoticeResponseDto(Notice entity) {
        this.noticeId = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.authorName = entity.getAuthor().getName();
        if (entity.getPhotos().size() > 0) {
            this.thumbnail = entity.getPhotos().get(0).getName();
        } else {
            this.thumbnail = null;
        }
    }
}
