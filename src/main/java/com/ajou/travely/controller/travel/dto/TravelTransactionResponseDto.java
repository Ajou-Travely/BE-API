package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TravelTransactionResponseDto {
    List<TravelTransactionResponseToSendDto> usersToSend = new ArrayList<>();
    List<TravelTransactionResponseToReceiveDto> usersToReceive = new ArrayList<>();

    @Builder
    public TravelTransactionResponseDto(List<TravelTransactionResponseToSendDto> usersToSend,
                                        List<TravelTransactionResponseToReceiveDto> usersToReceive) {
        this.usersToSend = usersToSend;
        this.usersToReceive = usersToReceive;
    }
}
