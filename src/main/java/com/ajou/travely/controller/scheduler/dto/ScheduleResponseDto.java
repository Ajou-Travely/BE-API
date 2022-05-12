package com.ajou.travely.controller.scheduler.dto;

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
    private Long scheduleId;
    private PlaceResponseDto place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<SimpleUserInfoDto> users;

    public ScheduleResponseDto(Schedule entity) {
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
