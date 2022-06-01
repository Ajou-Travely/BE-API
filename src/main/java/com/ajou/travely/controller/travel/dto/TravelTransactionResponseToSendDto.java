package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TravelTransactionResponseToSendDto {
    private SimpleUserInfoDto userToRecieve;
    private Long amount;

    @Builder
    public TravelTransactionResponseToSendDto(SimpleUserInfoDto userToRecieve, Long amount) {
        this.userToRecieve = userToRecieve;
        this.amount = amount;
    }
}
