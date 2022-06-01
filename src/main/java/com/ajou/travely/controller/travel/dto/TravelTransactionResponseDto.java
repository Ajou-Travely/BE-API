package com.ajou.travely.controller.travel.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TravelTransactionResponseDto {
    List<TravelTransactionResponseToSendDto> travelTransactionResponseToSendDtos = new ArrayList<>();
    List<TravelTransactionResponseToReceiveDto> travelTransactionResponseToReceiveDto = new ArrayList<>();

    @Builder
    public TravelTransactionResponseDto(List<TravelTransactionResponseToSendDto> travelTransactionResponseToSendDtos,
                                        List<TravelTransactionResponseToReceiveDto> travelTransactionResponseToReceiveDto) {
        this.travelTransactionResponseToSendDtos = travelTransactionResponseToSendDtos;
        this.travelTransactionResponseToReceiveDto = travelTransactionResponseToReceiveDto;
    }
}
