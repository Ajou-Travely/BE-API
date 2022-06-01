package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class TravelTransactionCreateRequestDto {
    @NotNull
    private Long senderId;
    @NotNull
    private Long receiverId;
    @NotNull
    private Long amount;

    @Builder
    public TravelTransactionCreateRequestDto(Long senderId,
                                             Long receiverId,
                                             Long amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }
}
