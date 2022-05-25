package com.ajou.travely.controller.cost.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
public class CostUpdateDto {
    private Long totalAmount;
    private String title;
    private String content;
    private Map<Long, CostUpdateInfoDto> amountsPerUser;
    private Long payerId;

    public CostUpdateDto(
            Long totalAmount,
            String title,
            String content,
            Map<Long, CostUpdateInfoDto> amountsPerUser,
            Long payerId
    ) {
        this.totalAmount = totalAmount;
        this.title = title;
        this.content = content;
        this.amountsPerUser = amountsPerUser;
        this.payerId = payerId;
    }
}
