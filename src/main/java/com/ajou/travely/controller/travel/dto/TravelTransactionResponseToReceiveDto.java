package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TravelTransactionResponseToReceiveDto {
    private SimpleUserInfoDto userToSend;
    private Long amount;

    @Builder
    public TravelTransactionResponseToReceiveDto(SimpleUserInfoDto userToSend, Long amount) {
        this.userToSend = userToSend;
        this.amount = amount;
    }
}
