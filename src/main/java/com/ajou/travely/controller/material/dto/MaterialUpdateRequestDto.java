package com.ajou.travely.controller.material.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MaterialUpdateRequestDto {
    private Long userId;
    private String material;
    private Boolean checked;

    @Builder
    public MaterialUpdateRequestDto(Long userId, String material, Boolean checked) {
        this.userId = userId;
        this.material = material;
        this.checked = checked;
    }
}