package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.Travel;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
public class TravelCreateRequestDto {
    @NotNull(message = "제목이 필요합니다.")
    private String title;
    @NotNull(message = "시작 날짜가 필요합니다.")
    private LocalDate startDate;
    @NotNull(message = "종료 날짜가 필요합니다.")
    private LocalDate endDate;
    @NotNull(message = "유저 아이디가 필요합니다.")
    private Long userId;

    public Travel toEntity() {
        return Travel.builder()
            .title(title)
            .startDate(startDate)
            .endDate(endDate)
                .managerId(userId)
            .build();
    }
}
