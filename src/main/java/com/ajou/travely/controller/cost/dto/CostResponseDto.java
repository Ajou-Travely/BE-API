package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.UserCost;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CostResponseDto {
    private final Long costId;
    private final Long totalAmount;
    private final String content;
    private final String title;
    private final Boolean isEquallyDivided;
    private final List<UserCostResponseDto> userCosts;
    private final Long payerId;

    public CostResponseDto(Cost entity) {
        this.costId = entity.getId();
        this.totalAmount = entity.getTotalAmount();
        this.content = entity.getContent();
        this.title = entity.getTitle();
        this.isEquallyDivided = entity.getIsEquallyDivided();
        this.userCosts = entity.getUserCosts().stream().map(userCost -> new UserCostResponseDto(
                userCost.getId(),
                userCost.getAmount(),
                new SimpleUserInfoDto(
                        userCost.getUser().getId(),
                        userCost.getUser().getName()
                ),
                userCost.getIsRequested()
        )).collect(Collectors.toList());
        this.payerId = entity.getPayerId();
    }
}
