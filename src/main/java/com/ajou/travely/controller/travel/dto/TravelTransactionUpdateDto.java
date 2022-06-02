package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TravelTransactionUpdateDto {
    private Long senderId;
    private Long receiverId;
    private Long amount;

    @Builder
    public TravelTransactionUpdateDto(Long senderId, Long receiverId, Long amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }
}
