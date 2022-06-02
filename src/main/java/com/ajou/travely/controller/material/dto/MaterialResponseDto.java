package com.ajou.travely.controller.material.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Material;
import com.ajou.travely.domain.user.User;
import lombok.Getter;

@Getter
public class MaterialResponseDto {
    private final SimpleUserInfoDto userInfo;
    private final String material;

    public MaterialResponseDto(Material entity) {
        this.userInfo = new SimpleUserInfoDto(entity.getUser());
        this.material = entity.getMaterial();
    }
}
