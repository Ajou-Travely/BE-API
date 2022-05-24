package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Branch;
import com.ajou.travely.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private final Long travelId;
    private final Long scheduleId;
    private final PlaceResponseDto place;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<SimpleUserInfoDto> users;

    public ScheduleResponseDto(Schedule entity) {
        this.travelId = entity.getTravel().getId();
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
    }
}
