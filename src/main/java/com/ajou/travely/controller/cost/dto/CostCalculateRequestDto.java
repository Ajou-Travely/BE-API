package com.ajou.travely.controller.cost.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CostCalculateRequestDto {
    private String[] receiverUuids;

    @Builder
    public CostCalculateRequestDto(String[] receiverUuids) {
        this.receiverUuids = receiverUuids;
    }
}
