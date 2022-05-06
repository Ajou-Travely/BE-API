package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.UserCost;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class CostsResponseDto {
    private Long costId;
    private Long totalAmount;
    private String title;
    private Map<Long, String> userInfo;
    private Long payerId;

    public CostsResponseDto(Long costId, Long totalAmount, String title, Map<Long, String> userInfo, Long payerId) {
        this.costId = costId;
        this.totalAmount = totalAmount;
        this.title = title;
        this.userInfo = userInfo;
        this.payerId = payerId;
    }
}
