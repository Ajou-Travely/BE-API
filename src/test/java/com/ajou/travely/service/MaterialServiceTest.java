package com.ajou.travely.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.ajou.travely.controller.material.dto.MaterialCreateRequestDto;
import com.ajou.travely.controller.material.dto.MaterialResponseDto;
import com.ajou.travely.controller.material.dto.MaterialUpdateRequestDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.domain.Material;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.repository.MaterialRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "auth.kakaoOauth2ClinetId=test",
    "auth.frontendRedirectUrl=test",
    "spring.mail.password=temptemptemptemp"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MaterialServiceTest {

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    UserService userService;

    @Autowired
    TravelService travelService;

    @Autowired
    MaterialService materialService;

    @DisplayName("준비물 생성")
    @Test
    void createMaterial() {
        // given
        User user1 = userService.insertUser(
            User.builder()
                .userType(UserType.USER)
                .email("sophoca@ajou.ac.kr")
                .name("홍성빈")
                .phoneNumber("112")
                .kakaoId(1L)
                .build()
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
            .builder()
            .title("test")
            .userEmails(new ArrayList<>())
            .startDate(LocalDate.of(2022, 5, 10))
            .endDate(LocalDate.of(2022, 5, 15))
            .build();

        Travel travel = travelService.createTravel(user1.getId(), request);

        // when
        MaterialResponseDto result = materialService.createMaterial(
            travel.getId(),
            MaterialCreateRequestDto
                .builder()
                .userId(user1.getId())
                .material("준비물1")
                .build());

        // then
        assertThat(result.getUserInfo().getUserName()).isEqualTo(user1.getName());
        assertThat(result.getMaterial()).isEqualTo("준비물1");
        assertThat(result.getChecked()).isEqualTo(false);
    }

    @DisplayName("준비물 수정")
    @Test
    void updateMaterial() {
        // given
        User user = userService.insertUser(
            User.builder()
                .userType(UserType.USER)
                .email("sophoca@ajou.ac.kr")
                .name("홍성빈")
                .phoneNumber("112")
                .kakaoId(1L)
                .build()
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
            .builder()
            .title("test")
            .userEmails(new ArrayList<>())
            .startDate(LocalDate.of(2022, 5, 10))
            .endDate(LocalDate.of(2022, 5, 15))
            .build();

        Travel travel = travelService.createTravel(user.getId(), request);

        MaterialResponseDto response = materialService.createMaterial(
            travel.getId(),
            MaterialCreateRequestDto
                .builder()
                .userId(user.getId())
                .material("준비물1")
                .build());

        // when
        User newUser = userService.insertUser(
            User.builder()
                .userType(UserType.USER)
                .email("errander@ajou.ac.kr")
                .name("이호용")
                .phoneNumber("119")
                .kakaoId(2L)
                .build()
        );
        MaterialResponseDto result = materialService.updateMaterial(
            response.getId(),
            MaterialUpdateRequestDto
                .builder()
                .userId(newUser.getId())
                .material("준비물2")
                .checked(true)
                .build()
            );

        // then
        assertThat(result.getUserInfo().getUserName()).isEqualTo(newUser.getName());
        assertThat(result.getMaterial()).isEqualTo("준비물2");
        assertThat(result.getChecked()).isEqualTo(true);
    }

    @Test
    void deleteMaterial() {
        // given
        User user = userService.insertUser(
            User.builder()
                .userType(UserType.USER)
                .email("sophoca@ajou.ac.kr")
                .name("홍성빈")
                .phoneNumber("112")
                .kakaoId(1L)
                .build()
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
            .builder()
            .title("test")
            .userEmails(new ArrayList<>())
            .startDate(LocalDate.of(2022, 5, 10))
            .endDate(LocalDate.of(2022, 5, 15))
            .build();

        Travel travel = travelService.createTravel(user.getId(), request);

        MaterialResponseDto response = materialService.createMaterial(
            travel.getId(),
            MaterialCreateRequestDto
                .builder()
                .userId(user.getId())
                .material("준비물1")
                .build());
        Long responseId = response.getId();

        // when
        Long resultId = materialService.deleteMaterial(travel.getId(), response.getId());

        // then
        assertThat(responseId).isEqualTo(resultId);

    }
}