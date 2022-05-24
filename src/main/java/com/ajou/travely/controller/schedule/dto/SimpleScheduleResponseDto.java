package com.ajou.travely.controller.schedule.dto;

import com.ajou.travely.controller.place.dto.SimplePlaceResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleScheduleResponseDto {
    private final Long scheduleId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final SimplePlaceResponseDto place;
    private final List<SimpleUserInfoDto> users;

    public SimpleScheduleResponseDto(Schedule entity) {
        this.scheduleId = entity.getId();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.place = new SimplePlaceResponseDto(entity.getPlace());
        this.users = entity
                .getBranches()
                .stream()
                .map(branch -> new SimpleUserInfoDto(branch.getUser()))
                .collect(Collectors.toList());
    }
}
