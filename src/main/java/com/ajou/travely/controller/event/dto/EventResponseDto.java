package com.ajou.travely.controller.event.dto;

import com.ajou.travely.controller.photo.dto.SimplePhotoResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Event;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class EventResponseDto {
    private final Long eventId;
    private final String title;
    private final String content;
    private final SimpleUserInfoDto authorInfo;
    private final LocalDateTime createdAt;
    private final List<SimplePhotoResponseDto> photoInfos;

    public EventResponseDto(Event entity) {
        this.eventId = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.authorInfo = new SimpleUserInfoDto(entity.getAuthor());
        this.createdAt = entity.getCreatedAt();
        if (entity.getPhotos() == null) {
            this.photoInfos = new ArrayList<>();
        } else {
            this.photoInfos = entity.getPhotos()
                    .stream()
                    .map(SimplePhotoResponseDto::new)
                    .collect(Collectors.toList());
        }
    }
}
