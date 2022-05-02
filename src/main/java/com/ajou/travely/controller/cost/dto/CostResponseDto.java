package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CostResponseDto {
    private Long id;
    private Travel travel;
    private Long totalAmount;
    private String content;
    private String title;
    private Boolean isEquallyDivided;
    private List<UserCost> userCosts;
    private User payer;

    public CostResponseDto(Cost entity) {
        this.id = entity.getId();
        this.travel = entity.getTravel();
        this.totalAmount = entity.getTotalAmount();
        this.content = entity.getContent();
        this.title = entity.getTitle();
        this.isEquallyDivided = entity.getIsEquallyDivided();
        this.payer = entity.getPayer();
        this.userCosts = entity.getUserCosts();
    }
}
