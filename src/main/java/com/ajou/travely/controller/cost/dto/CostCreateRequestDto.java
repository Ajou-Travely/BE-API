package com.ajou.travely.controller.cost.dto;

import com.ajou.travely.domain.Cost;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
public class CostCreateRequestDto {
    @NotNull(message = "총액이 필요합니다.")
    private Long totalAmount;
//    @NotNull(message = "여행 아이디가 필요합니다.")
//    private Long travelId;
    @NotNull(message = "제목이 필요합니다.")
    private String title;

    private String content;

    @NotNull(message = "사용자 별 금액에 대한 정보가 필요합니다.")
    private Map<Long, Long> amountsPerUser;
    @NotNull(message = "결제자의 아이디가 필요합니다.")
    private Long payerId;

    @Builder
    public CostCreateRequestDto(Long totalAmount,
                                String title,
                                String content,
                                Boolean isEquallyDivided,
                                Map<Long, Long> amountsPerUser,
                                Long payerId) {
        this.totalAmount = totalAmount;
        this.title = title;
        this.content = content;
        this.amountsPerUser = amountsPerUser;
        this.payerId = payerId;
    }
}
