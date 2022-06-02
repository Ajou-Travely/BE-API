package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.cost.TravelTransaction;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TravelTransactionCreateResponseDto {
    private Long id;
    private Long travelId;
    private Long senderId;
    private Long receiverId;
    private Long createdBy;
    private Long amount;

    @Builder
    public TravelTransactionCreateResponseDto(TravelTransaction entity) {
        this.id = entity.getId();
        this.travelId = entity.getTravel().getId();
        this.senderId = entity.getSender().getId();
        this.receiverId = entity.getReceiver().getId();
        this.createdBy = entity.getCreatedBy().getId();
        this.amount = entity.getAmount();
    }
}
