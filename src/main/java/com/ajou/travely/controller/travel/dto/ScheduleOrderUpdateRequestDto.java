package com.ajou.travely.controller.travel.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class ScheduleOrderUpdateRequestDto {
    @NotNull
    private final List<Long> scheduleOrder;

    public ScheduleOrderUpdateRequestDto(List<Long> scheduleOrder) {
        this.scheduleOrder = scheduleOrder;
    }
}
