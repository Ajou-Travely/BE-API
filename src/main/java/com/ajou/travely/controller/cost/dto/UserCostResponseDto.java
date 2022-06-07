package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.UserCost;
import lombok.Getter;

@Getter
public class UserCostResponseDto {
    private Long userCostId;
    private Long amount;
    private SimpleUserInfoDto simpleUserInfoDto;

    public UserCostResponseDto(UserCost entity) {
        this.userCostId = entity.getId();
        this.amount = entity.getAmount();
        this.simpleUserInfoDto = new SimpleUserInfoDto(entity.getUser());
    }
}
