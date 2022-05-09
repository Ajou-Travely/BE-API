package com.ajou.travely.controller.post.dto;

import com.ajou.travely.domain.Photo;
import lombok.Getter;

@Getter
public class SimplePhotoResponseDto {

    private final Long photoId;

    private final String path;

    public SimplePhotoResponseDto(Photo entity) {
        this.photoId = entity.getId();
        this.path = entity.getPath();
    }
}
