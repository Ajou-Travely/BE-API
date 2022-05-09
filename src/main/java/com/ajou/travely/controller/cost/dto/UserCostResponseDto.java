package com.ajou.travely.controller.cost.dto;

import lombok.Getter;

@Getter
public class UserCostResponseDto {
    private Long userCostId;
    private Long amount;
    private UserInfoResponseDto userInfoResponseDto;
    private Boolean isRequested;

    public UserCostResponseDto(
            Long userCostId,
            Long amount,
            UserInfoResponseDto userInfoResponseDto,
            Boolean isRequested
    ) {
        this.userCostId = userCostId;
        this.amount = amount;
        this.userInfoResponseDto = userInfoResponseDto;
        this.isRequested = isRequested;
    }
}
