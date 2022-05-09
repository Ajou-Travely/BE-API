package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class CostsWithUsersInTravelResponseDto {
    private List<User> usersByTravelId;
    private List<CostsResponseDto> costsResponseDtos;

    public CostsWithUsersInTravelResponseDto(
            List<User> usersByTravelId,
            List<CostsResponseDto> costsResponseDtos
    ) {
        this.usersByTravelId = usersByTravelId;
        this.costsResponseDtos = costsResponseDtos;
    }
}
