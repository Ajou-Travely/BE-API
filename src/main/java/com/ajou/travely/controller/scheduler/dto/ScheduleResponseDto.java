package com.ajou.travely.controller.scheduler.dto;

import com.ajou.travely.domain.Branch;
import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private Place place;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<User> users;

    public ScheduleResponseDto(Schedule entity) {
        this.id = entity.getId();
        this.place = entity.getPlace();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.users = entity.getBranches().stream().map(Branch::getUser).collect(Collectors.toList());
    }
}
