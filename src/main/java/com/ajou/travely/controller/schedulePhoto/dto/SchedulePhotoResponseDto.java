package com.ajou.travely.controller.schedulePhoto.dto;

import com.ajou.travely.domain.SchedulePhoto;
import lombok.Getter;

@Getter
public class SchedulePhotoResponseDto {
    private final Long schedulePhotoId;

    private final Long userId;

    private final Long scheduleId;

    private final String schedulePhotoPath;

    public SchedulePhotoResponseDto(SchedulePhoto entity) {
        this.schedulePhotoId = entity.getId();
        this.userId = entity.getUser().getId();
        this.scheduleId = entity.getSchedule().getId();
        this.schedulePhotoPath = entity.getPhotoPath();
    }
}
