package com.ajou.travely.controller.travel.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TravelTransactionUpdateDto {
    private Long senderId;
    private Long receiverId;
    private Long amount;
}
