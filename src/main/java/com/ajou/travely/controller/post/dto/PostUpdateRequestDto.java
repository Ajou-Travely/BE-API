package com.ajou.travely.controller.post.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateRequestDto {

    @NotBlank(message = "제목이 필요합니다.")
    private String title;

    private String text;

    private List<String> addedPhotoPaths;

    private List<Long> removedPhotoIds;

}
