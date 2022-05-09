package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import lombok.Getter;

@Getter
public class UserCostResponseDto {
    private Long userCostId;
    private Long amount;
    private SimpleUserInfoDto simpleUserInfoDto;
    private Boolean isRequested;

    public UserCostResponseDto(
            Long userCostId,
            Long amount,
            SimpleUserInfoDto simpleUserInfoDto,
            Boolean isRequested
    ) {
        this.userCostId = userCostId;
        this.amount = amount;
        this.simpleUserInfoDto = simpleUserInfoDto;
        this.isRequested = isRequested;
    }
}
