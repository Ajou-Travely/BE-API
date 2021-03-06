package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.cost.Cost;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CostResponseDto {
    private Long costId;
    private Long totalAmount;
    private String content;
    private String title;
    private List<UserCostResponseDto> userCosts;
    private Long payerId;

    public CostResponseDto(Cost entity) {
        this.costId = entity.getId();
        this.totalAmount = entity.getTotalAmount();
        this.content = entity.getContent();
        this.title = entity.getTitle();
        this.userCosts = entity.getUserCosts().stream().map(userCost -> {
            return new UserCostResponseDto(userCost);
        }).collect(Collectors.toList());
        this.payerId = entity.getPayerId();
    }
}
