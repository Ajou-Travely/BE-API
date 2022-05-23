package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.travel.Travel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ajou.travely.domain.UserTravel;
import lombok.Getter;

@Getter
public class TravelResponseDto {
    private final Long id;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String memo;
    private final Long managerId;
    private final List<SimpleUserInfoDto> users;
    private final List<SimpleScheduleResponseDto> schedules;
    private final List<Long> scheduleOrder;

    public TravelResponseDto(Travel entity, List<Schedule> schedules) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.memo = entity.getMemo();
        this.managerId = entity.getManagerId();
        this.users = entity
                .getUserTravels()
                .stream()
                .map(UserTravel::getUser)
                .map(SimpleUserInfoDto::new)
                .collect(Collectors.toList());
        this.schedules = schedules
                .stream()
                .map(SimpleScheduleResponseDto::new)
                .collect(Collectors.toList());
        if (entity.getScheduleOrder().isEmpty()) {
            this.scheduleOrder = new ArrayList<>();
        } else {
            this.scheduleOrder = Arrays
                    .stream(entity.getScheduleOrder().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }

    }
}
