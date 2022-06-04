package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.controller.schedulePhoto.dto.SchedulePhotoResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Branch;
import com.ajou.travely.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private final Long travelId;
    private final LocalDate date;
    private final Long scheduleId;
    private final PlaceResponseDto place;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final List<SimpleUserInfoDto> users;
    private final List<SchedulePhotoResponseDto> photos;

    public ScheduleResponseDto(Schedule entity) {
        this.travelId = entity.getTravelDate().getTravel().getId();
        this.date = entity.getTravelDate().getDate();
        this.scheduleId = entity.getId();
        this.place = new PlaceResponseDto(entity.getPlace());
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.users = entity
                .getBranches()
                .stream()
                .map(Branch::getUser)
                .map(SimpleUserInfoDto::new)
                .collect(Collectors.toList());
        this.photos = entity.getPhotos().stream()
                .map(SchedulePhotoResponseDto::new)
                .collect(Collectors.toList());
    }
}
