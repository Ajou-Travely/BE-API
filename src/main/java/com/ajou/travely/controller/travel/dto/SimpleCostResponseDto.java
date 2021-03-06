package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.cost.Cost;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleCostResponseDto {
    private Long costId;
    private Long totalAmount;
    private String title;
    private List<Long> userIds;
    private Long payerId;

    public SimpleCostResponseDto(Cost entity) {
        this.costId = entity.getId();
        this.totalAmount = entity.getTotalAmount();
        this.title = entity.getTitle();
        this.userIds = entity.getUserCosts().stream().map(uc -> uc.getUser().getId()).collect(Collectors.toList());
        this.payerId = entity.getPayerId();
    }
}
