package com.ajou.travely.controller.travel.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleOrderUpdateRequestDto {
    @NotNull
    private List<Long> scheduleOrder;

    public ScheduleOrderUpdateRequestDto(List<Long> scheduleOrder) {
        this.scheduleOrder = scheduleOrder;
    }
}
