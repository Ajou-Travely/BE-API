package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class CostCreateResponseDto {
    private final Long id;
    private final Travel travel;
    private final Long totalAmount;
    private final String content;
    private final String title;
    private final Boolean isEquallyDivided;
    private final List<UserCost> userCosts;
    private final User payer;

    public CostCreateResponseDto(Cost entity, User payer) {
        this.id = entity.getId();
        this.travel = entity.getTravel();
        this.totalAmount = entity.getTotalAmount();
        this.content = entity.getContent();
        this.title = entity.getTitle();
        this.isEquallyDivided = entity.getIsEquallyDivided();
        this.payer = payer;
        this.userCosts = entity.getUserCosts();
    }
}
