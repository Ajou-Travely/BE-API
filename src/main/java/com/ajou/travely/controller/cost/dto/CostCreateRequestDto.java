package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.domain.Cost;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CostCreateRequestDto {
    @NotNull(message = "총액이 필요합니다.")
    private final Long totalAmount;
    @NotNull(message = "여행 아이디가 필요합니다.")
    private final Long travelId;
    @NotNull(message = "제목이 필요합니다.")
    private final String title;
    private final String content;
    @NotNull(message = "일괄 정산 여부가 필요합니다.")
    private final Boolean isEquallyDivided;
    @NotNull(message = "사용자 별 금액에 대한 정보가 필요합니다.")
    private final Map<Long, Long> amountsPerUser;
    @NotNull(message = "결제자의 아이디가 필요합니다.")
    private final Long payerId;

    @Builder
    public CostCreateRequestDto(Long totalAmount,
                                Long travelId,
                                String title,
                                String content,
                                Boolean isEquallyDivided,
                                Map<Long, Long> amountsPerUser,
                                Long payerId) {
        this.totalAmount = totalAmount;
        this.travelId = travelId;
        this.title = title;
        this.content = content;
        this.isEquallyDivided = isEquallyDivided;
        this.amountsPerUser = amountsPerUser;
        this.payerId = payerId;
    }
}
