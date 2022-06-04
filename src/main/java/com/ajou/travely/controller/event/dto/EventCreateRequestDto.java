package com.ajou.travely.controller.event.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
public class EventCreateRequestDto {
    @NotBlank(message = "제목이 필요합니다.")
    private String title;
    private String content;
    private List<MultipartFile> photos = new ArrayList<>();

    @Builder
    public EventCreateRequestDto(String title, String content, List<MultipartFile> photos) {
        this.title = title;
        this.content = content;
        this.photos = photos;
    }
}
