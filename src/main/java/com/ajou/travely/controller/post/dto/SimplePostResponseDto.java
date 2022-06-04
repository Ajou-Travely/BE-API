package com.ajou.travely.controller.post.dto;

import com.ajou.travely.controller.photo.dto.SimplePhotoResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimplePostResponseDto {

    private final Long postId;

    private final Long scheduleId;

    private final SimpleUserInfoDto userInfo;

    private final String title;

    private final String text;

    private final LocalDateTime createdAt;

    public SimplePostResponseDto(Post entity) {
        this.postId = entity.getId();
        this.scheduleId = entity.getSchedule().getId();
        this.userInfo = new SimpleUserInfoDto(entity.getUser());
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.createdAt = entity.getCreatedAt();
    }

}
