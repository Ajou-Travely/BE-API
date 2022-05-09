package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.UserCost;
import lombok.Getter;

import java.util.List;

@Getter
public class CostsResponseDto {
    private Long costId;
    private Long totalAmount;
    private String title;
    private List<Long> userIds;
    private Long payerId;

    public CostsResponseDto(Long costId, Long totalAmount, String title, List<Long> userIds, Long payerId) {
        this.costId = costId;
        this.totalAmount = totalAmount;
        this.title = title;
        this.userIds = userIds;
        this.payerId = payerId;
    }
}
