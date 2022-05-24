package com.ajou.travely.controller.cost.dto;

import lombok.Getter;

@Getter
public class CostUpdateInfoDto {
    private final Long amount;
    private final Boolean isRequested;

    public CostUpdateInfoDto(Long amount, Boolean isRequested) {
        this.amount = amount;
        this.isRequested = isRequested;
    }
}
