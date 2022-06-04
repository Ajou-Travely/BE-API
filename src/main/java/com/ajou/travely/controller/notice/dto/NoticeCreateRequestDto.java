package com.ajou.travely.controller.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
public class NoticeCreateRequestDto {
    @NotBlank(message = "제목이 필요합니다.")
    private String title;
    private String content;
    private List<MultipartFile> photos = new ArrayList<>();

    @Builder
    public NoticeCreateRequestDto(String title, String content, List<MultipartFile> photos) {
        this.title = title;
        this.content = content;
        this.photos = photos;
    }
}
