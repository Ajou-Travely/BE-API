package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.domain.UserCost;
import lombok.Getter;

import java.util.List;

@Getter
public class CostResponseDto {
    private Long costId;
    private Long totalAmount;
    private String content;
    private String title;
    private Boolean isEquallyDivided;
    private List<UserCostResponseDto> userCostResponseDtos;
    private Long payerId;

    public CostResponseDto(
            Long costId,
            Long totalAmount,
            String content,
            String title,
            Boolean isEquallyDivided,
            List<UserCostResponseDto> userCostResponseDtos,
            Long payerId
    ) {
        this.costId = costId;
        this.totalAmount = totalAmount;
        this.content = content;
        this.title = title;
        this.isEquallyDivided = isEquallyDivided;
        this.userCostResponseDtos = userCostResponseDtos;
        this.payerId = payerId;
    }
}
