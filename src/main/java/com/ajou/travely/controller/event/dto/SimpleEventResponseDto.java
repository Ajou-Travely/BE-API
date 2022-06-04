package com.ajou.travely.controller.event.dto;

import com.ajou.travely.domain.Event;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleEventResponseDto {
    private final Long eventId;
    private final String title;
    private final String content;
    private final String authorName;
    private final String thumbnail;

    @Builder
    public SimpleEventResponseDto(Event entity) {
        this.eventId = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.authorName = entity.getAuthor().getName();
        if (entity.getPhotos().size() > 0) {
            this.thumbnail = entity.getPhotos().get(0).getName();
        } else {
            this.thumbnail = null;
        }
    }
}
