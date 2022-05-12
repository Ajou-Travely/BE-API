package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.domain.Place;
import com.ajou.travely.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Transactional
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
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
                        .x(placeCreateRequestDto.getX())
                        .y(placeCreateRequestDto.getY())
                        .addressName(placeCreateRequestDto.getAddressName())
                        .addressRoadName(placeCreateRequestDto.getAddressRoadName())
                        .phoneNumber(placeCreateRequestDto.getPhoneNumber())
                        .build()
        );
    }

    @Transactional
    public Place findPlaceById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("해당 id의 장소가 없습니다."));
    }

    @Transactional
    public List<Place> findPlacesByName(String placeName) {
        return placeRepository.findByName(placeName);
    }

    @Transactional
    public void deleteAllPlaces() {
        placeRepository.deleteAll();
    }
}
