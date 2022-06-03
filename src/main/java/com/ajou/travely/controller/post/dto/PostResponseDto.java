package com.ajou.travely.controller.post.dto;

import com.ajou.travely.controller.photo.dto.SimplePhotoResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long postId;

    private final Long scheduleId;

    private final SimpleUserInfoDto userInfo;

    private final String title;

    private final String text;

    private final LocalDateTime createdAt;

    private final List<CommentResponseDto> comments;

    private final List<SimplePhotoResponseDto> photoInfos;

    public PostResponseDto(Post entity) {
        this.postId = entity.getId();
        this.scheduleId = entity.getSchedule().getId();
        this.userInfo = new SimpleUserInfoDto(entity.getUser());
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.createdAt = entity.getCreatedAt();
        this.comments = entity.getComments()
            .stream()
            .map(CommentResponseDto::new)
            .collect(Collectors.toList());
        this.photoInfos = entity.getPhotos()
            .stream()
            .map(SimplePhotoResponseDto::new)
            .collect(Collectors.toList());

    }

}
