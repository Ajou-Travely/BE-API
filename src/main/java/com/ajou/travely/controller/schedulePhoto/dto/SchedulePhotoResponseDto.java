package com.ajou.travely.controller.schedulePhoto.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.SchedulePhoto;
import lombok.Getter;

@Getter
public class SchedulePhotoResponseDto {
    private final Long schedulePhotoId;

    private final SimpleUserInfoDto userInfoDto;

    private final Long scheduleId;

    private final String schedulePhotoPath;

    public SchedulePhotoResponseDto(SchedulePhoto entity) {
        this.schedulePhotoId = entity.getId();
        this.userInfoDto = new SimpleUserInfoDto(entity.getUser());
        this.scheduleId = entity.getSchedule().getId();
        this.schedulePhotoPath = entity.getPhotoPath();
    }
}
