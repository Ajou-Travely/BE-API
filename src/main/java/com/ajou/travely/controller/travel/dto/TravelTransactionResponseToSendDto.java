package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TravelTransactionResponseToSendDto {
    private Long travelTransactionId;
    private SimpleUserInfoDto userToRecieve;
    private Long amount;

    @Builder
    public TravelTransactionResponseToSendDto(Long travelTransactionId, SimpleUserInfoDto userToRecieve, Long amount) {
        this.travelTransactionId = travelTransactionId;
        this.userToRecieve = userToRecieve;
        this.amount = amount;
    }
}
