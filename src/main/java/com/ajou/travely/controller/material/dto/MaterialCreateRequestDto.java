package com.ajou.travely.controller.material.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MaterialCreateRequestDto {
    private Long userId;
    private String material;

    @Builder
    public MaterialCreateRequestDto(Long userId, String material) {
        this.userId = userId;
        this.material = material;
    }
}
