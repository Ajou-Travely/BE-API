package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.UserCost;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleCostResponseDto {
    private final Long costId;
    private final Long totalAmount;
    private final String title;
    private final List<Long> userIds;
    private final Long payerId;

    public SimpleCostResponseDto(Cost entity) {
        this.costId = entity.getId();
        this.totalAmount = entity.getTotalAmount();
        this.title = entity.getTitle();
        this.userIds = entity.getUserCosts().stream().map(uc -> uc.getUser().getId()).collect(Collectors.toList());
        this.payerId = entity.getPayerId();
    }
}
