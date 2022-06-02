package com.ajou.travely.material.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MaterialCreateOrUpdateRequestDto {
    private final Long userId;
    private final String material;

    @Builder
    public MaterialCreateOrUpdateRequestDto(Long userId, String material) {
        this.userId = userId;
        this.material = material;
    }
}
