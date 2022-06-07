package com.ajou.travely.controller.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class EventUpdateDto {
    private String title;
    private String content;
    private List<MultipartFile> photos = new ArrayList<>();
    private List<Long> photoIdsToRemove = new ArrayList<>();

    @Builder
    public EventUpdateDto(String title,
                           String content,
                           List<MultipartFile> photos,
                           List<Long> photoIdsToRemove) {
        this.title = title;
        this.content = content;
        this.photos = photos;
        this.photoIdsToRemove = photoIdsToRemove;
    }
}
