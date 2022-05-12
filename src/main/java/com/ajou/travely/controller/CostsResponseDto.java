package com.ajou.travely.controller;

import com.ajou.travely.domain.UserCost;
import lombok.Getter;

import java.util.List;

@Getter
public class CostsResponseDto {
    private Long costId;
    private Long totalAmount;
    private String title;
    private List<UserCost> userCosts;
    private Long payerId;

    public CostsResponseDto(Long costId, Long totalAmount, String title, List<UserCost> userCosts, Long payerId) {
        this.costId = costId;
        this.totalAmount = totalAmount;
        this.title = title;
        this.userCosts = userCosts;
        this.payerId = payerId;
    }

    @Override
    public String toString() {
        return "CostsResponseDto{" +
                "costId=" + costId +
                ", totalAmount=" + totalAmount +
                ", title='" + title + '\'' +
                ", userCosts=" + userCosts +
                ", payerId=" + payerId +
                '}';
    }
}