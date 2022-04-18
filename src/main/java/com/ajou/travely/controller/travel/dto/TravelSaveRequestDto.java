package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.Travel;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TravelSaveRequestDto {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String memo;

    @Builder
    public TravelSaveRequestDto(String title, String memo, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.memo = memo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Travel toEntity() {
        return Travel.builder()
            .title(title)
            .memo(memo)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }
}
