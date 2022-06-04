package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.material.dto.MaterialResponseDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.UserTravel;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.travel.TravelDate;
import com.ajou.travely.domain.travel.TravelType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TravelResponseDto {
    private final Long id;
    private final String title;
    private final String memo;
    private final Integer budget;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long managerId;
    private final TravelType travelType;
    private final List<SimpleUserInfoDto> users;
    private final List<SimpleTravelDateResponseDto> dates;
    private final List<CostResponseDto> costs;
    private final List<MaterialResponseDto> materials;

    public TravelResponseDto(Travel entity, List<TravelDate> dates, List<CostResponseDto> costs) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.memo = entity.getMemo();
        this.budget = entity.getBudget();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.managerId = entity.getManagerId();
        this.travelType = entity.getTravelType();
        this.users = entity
            .getUserTravels()
            .stream()
            .map(UserTravel::getUser)
            .map(SimpleUserInfoDto::new)
            .collect(Collectors.toList());
        this.dates = dates
            .stream()
            .map(SimpleTravelDateResponseDto::new)
            .collect(Collectors.toList());
        this.materials = entity
            .getMaterials()
            .stream()
            .map(MaterialResponseDto::new)
            .collect(Collectors.toList());
        this.costs = costs;
    }
}
