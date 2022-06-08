package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.controller.travel.dto.SimpleTravelResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.cost.Cost;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CostCreateResponseDto {
    private Long id;
    private Long totalAmount;
    private String content;
    private String title;
    private List<UserCostResponseDto> userCosts;
    private Long payerId;

    public CostCreateResponseDto(Cost entity, User payer) {
        this.id = entity.getId();
        this.totalAmount = entity.getTotalAmount();
        this.content = entity.getContent();
        this.title = entity.getTitle();
        this.payerId = payer.getId();
        this.userCosts = entity.getUserCosts()
                .stream().map(UserCostResponseDto::new)
                .collect(Collectors.toList());
    }
}
