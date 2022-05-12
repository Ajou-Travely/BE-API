package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.ajou.travely.domain.UserTravel;
import lombok.Getter;

@Getter
public class TravelResponseDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String memo;
    private Long managerId;
    private List<SimpleUserInfoDto> users;
    private List<SimpleScheduleResponseDto> schedules;

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
    }
}
