package com.ajou.travely.controller.photo.dto;

import com.ajou.travely.domain.Photo;
import lombok.Getter;

@Getter
public class SimplePhotoResponseDto {

    private final Long photoId;

    private final String name;

    public SimplePhotoResponseDto(Photo entity) {
        this.photoId = entity.getId();
        this.name = entity.getName();
    }
}
