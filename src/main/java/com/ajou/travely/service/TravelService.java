package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.TravelSaveRequestDto;
import com.ajou.travely.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;

    @Transactional
    public Long save(TravelSaveRequestDto requestDto) {
        return travelRepository.save(requestDto.toEntity()).getId();
    }
}
