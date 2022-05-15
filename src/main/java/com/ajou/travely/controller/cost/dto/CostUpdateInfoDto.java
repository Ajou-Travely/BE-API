package com.ajou.travely.controller.cost.dto;

import lombok.Getter;

@Getter
public class CostUpdateInfoDto {
    private Long amount;
    private Boolean isRequested;

    public CostUpdateInfoDto(Long amount, Boolean isRequested) {
        this.amount = amount;
        this.isRequested = isRequested;
    }
}
