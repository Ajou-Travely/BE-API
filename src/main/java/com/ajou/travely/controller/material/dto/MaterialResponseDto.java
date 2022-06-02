package com.ajou.travely.controller.material.dto;

import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Material;
import lombok.Getter;

@Getter
public class MaterialResponseDto {
    private final Long id;
    private final SimpleUserInfoDto userInfo;
    private final String material;
    private final boolean checked;

    public MaterialResponseDto(Material entity) {
        this.id = entity.getId();
        this.userInfo = new SimpleUserInfoDto(entity.getUser());
        this.material = entity.getMaterial();
        this.checked = entity.getChecked();
    }
}
