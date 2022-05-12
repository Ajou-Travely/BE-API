package com.ajou.travely.controller.travel.dto;

import com.ajou.travely.domain.Travel;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@Getter
public class TravelCreateRequestDto {
    @NotNull(message = "제목이 필요합니다.")
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "시작 날짜가 필요합니다.")
    private LocalDate startDate;
    @NotNull(message = "종료 날짜가 필요합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotNull(message = "유저 아이디가 필요합니다.")
    private Long userId;

    @Builder
    public TravelCreateRequestDto(
            @NonNull String title
            , @NonNull LocalDate startDate
            , @NonNull LocalDate endDate
            , @NonNull Long userId
    ) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }
}
