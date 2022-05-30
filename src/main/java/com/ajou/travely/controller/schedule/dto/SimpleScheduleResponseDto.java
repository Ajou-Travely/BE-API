package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.controller.schedulePhoto.dto.SchedulePhotoResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Schedule;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleScheduleResponseDto {

    private final Long scheduleId;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final PlaceResponseDto place;
    private final List<SimpleUserInfoDto> users;
    private List<SchedulePhotoResponseDto> photos;

    public SimpleScheduleResponseDto(Schedule entity) {
        this.scheduleId = entity.getId();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.place = new PlaceResponseDto(entity.getPlace());
        this.users = entity
                .getBranches()
                .stream()
                .map(branch -> new SimpleUserInfoDto(branch.getUser()))
                .collect(Collectors.toList());
        this.photos = entity.getPhotos().stream()
            .map(SchedulePhotoResponseDto::new)
            .collect(Collectors.toList());
    }

}
