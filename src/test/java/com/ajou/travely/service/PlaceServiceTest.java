package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceResponseDto;
import com.ajou.travely.domain.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
        "spring.mail.password=temptemptemptemp"
})
@Transactional
class PlaceServiceTest {
    @Autowired
    PlaceService placeService;

    Place ajouUniv;
    Place inhaUniv;

    @BeforeEach
    public void setUp() {
        ajouUniv = placeService.insertPlace(
                Place.builder()
                        .lat(4.5)
                        .lng(5.4)
                        .placeUrl("ajou.ac.kr")
                        .placeName("아주대학교")
                        .addressName("원천동")
                        .addressRoadName("원천로")
                        .kakaoMapId(1L)
                        .build());
        inhaUniv = placeService.insertPlace(
                Place.builder()
                        .lat(3.7)
                        .lng(7.3)
                        .placeUrl("inha.ac.kr")
                        .placeName("인하대학교")
                        .addressName("인천")
                        .addressRoadName("인천로")
                        .phoneNumber("119")
                        .kakaoMapId(2L)
                        .build());
    }
    @Test
    @DisplayName("생성한 Place를 DB에 삽입할 수 있다.")
    @Rollback
    public void insertTest() {
        List<PlaceResponseDto> places = placeService.getAllPlaces();

        assertThat(places).hasSize(2);
    }

    @Test
    @DisplayName("생성한 place를 id로 검색할 수 있다.")
    @Rollback
    public void findByIDTest() {
        PlaceResponseDto findAjou = placeService.findPlaceById(ajouUniv.getId());
        PlaceResponseDto findInha = placeService.findPlaceById(inhaUniv.getId());

        assertThat(findAjou.getPlaceUrl()).isEqualTo(ajouUniv.getPlaceUrl());
        assertThat(findInha.getPlaceName()).isEqualTo(inhaUniv.getPlaceName());
    }

    @Test
    @DisplayName("생성한 place를 이름으로 검색할 수 있다.")
    @Rollback
    public void findByNameTest() {
        Place ajoudon = Place.builder()
                .lat(2.6)
                .lng(6.2)
                .placeUrl("naver.com")
                .placeName("원천아주돈")
                .addressName("인계동")
                .addressRoadName("인계로")
                .kakaoMapId(3L)
                .build();
        placeService.insertPlace(ajoudon);

        List<PlaceResponseDto> ajouPlaces = placeService.findPlacesByName("아주");

        assertThat(ajouPlaces).hasSize(2);
    }
}