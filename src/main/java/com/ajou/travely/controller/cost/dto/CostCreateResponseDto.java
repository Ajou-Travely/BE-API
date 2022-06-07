package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.cost.Cost;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class CostCreateResponseDto {
    private Long id;
    private SimpleTravelResponseDto travel;
    private Long totalAmount;
    private String content;
    private String title;
    private List<UserCost> userCosts;
    private SimpleUserInfoDto payer;

    public CostCreateResponseDto(Cost entity, User payer) {
        this.id = entity.getId();
        this.travel = new SimpleTravelResponseDto(entity.getTravel());
        this.totalAmount = entity.getTotalAmount();
        this.content = entity.getContent();
        this.title = entity.getTitle();
        this.payer = new SimpleUserInfoDto(payer);
        this.userCosts = entity.getUserCosts();
    }
}
