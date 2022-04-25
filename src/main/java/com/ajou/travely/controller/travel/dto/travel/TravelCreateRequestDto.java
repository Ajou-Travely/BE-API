package com.ajou.travely.controller.travel.dto.travel;

import com.ajou.travely.domain.Travel;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TravelCreateRequestDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;

    @Builder
    public TravelCreateRequestDto(String title, LocalDate startDate, LocalDate endDate, Long userId) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public Travel toEntity() {
        return Travel.builder()
            .title(title)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }
}
