package com.ajou.travely.controller.comment.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long commentId;

    private final SimpleUserInfoDto userInfo;

    private final String content;

    public CommentResponseDto(Comment entity) {
        this.commentId = entity.getId();
        this.userInfo = new SimpleUserInfoDto(entity.getUser());
        this.content = entity.getContent();
    }

}
