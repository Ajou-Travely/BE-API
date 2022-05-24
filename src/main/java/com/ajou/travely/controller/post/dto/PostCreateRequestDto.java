package com.ajou.travely.controller.post.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostCreateRequestDto {

    @NotNull(message = "schedule Id가 필요합니다.")
    private final Long scheduleId;
    @NotBlank(message = "제목이 필요합니다.")
    private final String title;
    @NotBlank
    private final String text;
    @NotNull
    private final List<MultipartFile> photos;

    @Builder
    public PostCreateRequestDto(Long scheduleId,
                                String title,
                                String text,
                                List<MultipartFile> photos) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.text = text;
        this.photos = photos;
    }
}
