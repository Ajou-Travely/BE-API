package com.ajou.travely.controller.post.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostUpdateRequestDto {

    @NotBlank(message = "제목이 필요합니다.")
    private final String title;
    private final String text;
    private final List<MultipartFile> addPhotos;
    private final List<Long> removePhotoIds;

    @Builder
    public PostUpdateRequestDto(String title,
                                String text,
                                List<MultipartFile> addPhotos,
                                List<Long> removePhotoIds) {
        this.title = title;
        this.text = text;
        this.addPhotos = addPhotos;
        this.removePhotoIds = removePhotoIds;
    }
}
