package com.ajou.travely.controller.cost.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class CostUpdateDto {
    private Long totalAmount;
    private String title;
    private String content;
    private Boolean isEquallyDivided;
    private Map<Long, CostUpdateInfoDto> amountsPerUser;
    private Long payerId;

    public CostUpdateDto(
            Long totalAmount,
            String title,
            String content,
            Boolean isEquallyDivided,
            Map<Long, CostUpdateInfoDto> amountsPerUser,
            Long payerId
    ) {
        this.totalAmount = totalAmount;
        this.title = title;
        this.content = content;
        this.isEquallyDivided = isEquallyDivided;
        this.amountsPerUser = amountsPerUser;
        this.payerId = payerId;
    }
}
