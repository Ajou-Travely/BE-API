package com.ajou.travely.service;

import com.ajou.travely.controller.material.dto.MaterialCreateRequestDto;
import com.ajou.travely.controller.material.dto.MaterialResponseDto;
import com.ajou.travely.controller.material.dto.MaterialUpdateRequestDto;
import com.ajou.travely.domain.Material;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.MaterialRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MaterialService {

    private final TravelRepository travelRepository;

    private final UserRepository userRepository;

    private final MaterialRepository materialRepository;

    public MaterialResponseDto createMaterial(Long travelId, MaterialCreateRequestDto requestDto) {
        Travel travel = checkTravelRecord(travelId);
        User user = requestDto.getUserId() == null
            ? null
            : checkUserRecord(requestDto.getUserId());
        Material material = materialRepository.save(
            Material.builder()
                .travel(travel)
                .user(user)
                .material(requestDto.getMaterial())
                .checked(false)
                .build()
        );
        travel.addMaterial(material);
        return new MaterialResponseDto(material);
    }

    public MaterialResponseDto updateMaterial(Long materialId, MaterialUpdateRequestDto requestDto) {
        Material material = checkMaterialRecord(materialId);
        User user = requestDto.getUserId() == null
            ? null
            : checkUserRecord(requestDto.getUserId());
        material.updateMaterial(user, requestDto.getMaterial(), requestDto.getChecked());
        return new MaterialResponseDto(material);
    }

    public Long deleteMaterial(Long travelId, Long materialId) {
        Travel travel = checkTravelRecord(travelId);
        Material material = checkMaterialRecord(materialId);
        travel.removeMaterial(material);
        materialRepository.delete(material);
        return materialId;
    }

    /* -------------------------------------------------------------------------- */

    private Travel checkTravelRecord(Long travelId) {
        return checkRecord(
            travelRepository.findById(travelId),
            "?????? ID??? Travel??? ???????????? ????????????.",
            ErrorCode.TRAVEL_NOT_FOUND
        );
    }

    private User checkUserRecord(Long userId) {
        return checkRecord(
            userRepository.findById(userId),
            "?????? ID??? User??? ???????????? ????????????.",
            ErrorCode.USER_NOT_FOUND
        );
    }

    private Material checkMaterialRecord(Long materialId) {
        return checkRecord(
            materialRepository.findById(materialId),
            "?????? ID??? Material??? ???????????? ????????????.",
            ErrorCode.MATERIAL_NOT_FOUND
        );
    }

    private <T> T checkRecord(Optional<T> record, String message, ErrorCode code) {
        return record.orElseThrow(() ->
            new RecordNotFoundException(message, code));
    }

}
