package com.ajou.travely.controller.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public CommentCreateRequestDto(@NotBlank String content) {
        this.content = content;
    }
}
