package com.ajou.travely.controller.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentUpdateDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public CommentUpdateDto(@NotBlank String content) {
        this.content = content;
    }
}
