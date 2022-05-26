package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.domain.Place;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Transactional
    public List<PlaceResponseDto> getAllPlaces() {
        return placeRepository
                .findAll()
                .stream()
                .map(PlaceResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Place insertPlace(Place place) {
        return placeRepository.save(place);
    }

    @Transactional
    public PlaceResponseDto createPlace(PlaceCreateRequestDto placeCreateRequestDto) {
        return new PlaceResponseDto(
                Place.builder()
                        .placeName(placeCreateRequestDto.getPlaceName())
                        .placeUrl(placeCreateRequestDto.getPlaceUrl())
                        .lat(placeCreateRequestDto.getLat())
                        .lng(placeCreateRequestDto.getLng())
                        .addressName(placeCreateRequestDto.getAddressName())
                        .addressRoadName(placeCreateRequestDto.getAddressRoadName())
                        .phoneNumber(placeCreateRequestDto.getPhoneNumber())
                        .build()
        );
    }

    @Transactional
    public PlaceResponseDto findPlaceById(Long placeId) {
        return new PlaceResponseDto(
                placeRepository.findById(placeId)
                        .orElseThrow(() -> new RecordNotFoundException("해당 id의 장소가 존재하지 않습니다.", ErrorCode.PLACE_NOT_FOUND))
        );
    }

    @Transactional
    public List<PlaceResponseDto> findPlacesByName(String placeName) {
        return placeRepository
                .findByName(placeName)
                .stream()
                .map(PlaceResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAllPlaces() {
        placeRepository.deleteAll();
    }
}
