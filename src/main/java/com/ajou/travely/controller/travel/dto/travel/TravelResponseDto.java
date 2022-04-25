package com.ajou.travely.controller.travel.dto.travel;

import com.ajou.travely.controller.travel.dto.user.SimpleUserInfoDto;
import com.ajou.travely.domain.Travel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ajou.travely.domain.UserTravel;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

@Getter
public class TravelResponseDto {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String memo;
    private List<SimpleUserInfoDto> users;

    public TravelResponseDto(Travel entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.memo = entity.getMemo();
        this.users = new ArrayList<>();
        for (UserTravel userTravel:entity.getUserTravels()) {
            SimpleUserInfoDto user = new SimpleUserInfoDto(userTravel.getUser().getId(), userTravel.getUser().getName());
            users.add(user);
        }
    }
}