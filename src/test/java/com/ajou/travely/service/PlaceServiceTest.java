package com.ajou.travely.service;

import com.ajou.travely.domain.Place;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
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
                        .x(4.5)
                        .y(5.4)
                        .placeUrl("ajou.ac.kr")
                        .placeName("아주대학교")
                        .addressName("원천동")
                        .addressRoadName("원천로")
                        .build());
        inhaUniv = placeService.insertPlace(
                Place.builder()
                        .x(3.7)
                        .y(7.3)
                        .placeUrl("inha.ac.kr")
                        .placeName("인하대학교")
                        .addressName("인천")
                        .addressRoadName("인천로")
                        .phoneNumber("119")
                        .build());
    }

    @AfterEach
    public void cleanUp() {
        placeService.deleteAllPlaces();
    }

    @Test
    @DisplayName("생성한 Place를 DB에 삽입할 수 있다.")
    @Rollback
    public void insertTest() {
        List<Place> places = placeService.getAllPlaces();

        Assertions.assertThat(places).hasSize(2);
    }

    @Test
    @DisplayName("생성한 place를 id로 검색할 수 있다.")
    @Rollback
    public void findByIDTest() {
        Place findAjou = placeService.findPlaceById(ajouUniv.getId());
        Place findInha = placeService.findPlaceById(inhaUniv.getId());

        assertThat(findAjou, samePropertyValuesAs(ajouUniv));
        assertThat(findInha, samePropertyValuesAs(inhaUniv));
    }

    @Test
    @DisplayName("생성한 place를 이름으로 검색할 수 있다.")
    @Rollback
    public void findByNameTest() {
        Place ajoudon = Place.builder()
                .x(2.6)
                .y(6.2)
                .placeUrl("naver.com")
                .placeName("원천아주돈")
                .addressName("인계동")
                .addressRoadName("인계로")
                .build();
        placeService.insertPlace(ajoudon);

        List<Place> ajouPlaces = placeService.findPlacesByName("아주");

        assertThat(ajouPlaces, hasSize(2));
    }
}