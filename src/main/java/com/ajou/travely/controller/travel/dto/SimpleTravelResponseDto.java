package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserTravel;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleTravelResponseDto {
    private final Long id;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Long managerId;
    private final List<SimpleUserInfoDto> users;

    public SimpleTravelResponseDto(Travel entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.managerId = entity.getManagerId();
        this.users = entity
                .getUserTravels()
                .stream()
                .map(UserTravel::getUser)
                .map(SimpleUserInfoDto::new)
                .collect(Collectors.toList());
    }
}
