package com.ajou.travely.service;

import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ScheduleServiceTest {
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    PlaceService placeService;

    @Autowired
    UserService userService;

    @Autowired
    TravelService travelService;

    Place ajouUniv;
    Place inhaUniv;
    User user;
    Travel travel;

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
        user = userService.insertUser(
                User.builder()
                        .name("test")
                        .phoneNumber("111")
                        .type(Type.USER)
                        .email("test@test")
                        .kakaoId(0L)
                        .build());
        travel = travelService.insertTravel(
                Travel.builder()
                        .managerId(user.getId())
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .title("첫 여행")
                        .build());
    }

    @Test
    @DisplayName("Schedule을 생성할 수 있다.")
    @Rollback
    public void testCreateSchedule() {
        User user1 = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(1L)
                        .build()
        );
        User user2 = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travel.getId(), user1.getId());
        travelService.addUserToTravel(travel.getId(), user2.getId());
        Long scheduleId = scheduleService.createSchedule(
                new ScheduleCreateRequestDto(travel.getId(),
                        ajouUniv.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId()))
                )
        );
        ScheduleResponseDto schedule = scheduleService.getScheduleById(scheduleId);
        assertThat(schedule.getPlace().getPlaceId()).isEqualTo(ajouUniv.getId());
        assertThat(schedule.getUsers()).hasSize(3);
    }

    @Test
    @DisplayName("Schedule을 업데이트할 수 있다.")
    @Rollback
    public void testUpdateSchedule() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        User user1 = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(1L)
                        .build()
        );
        User user2 = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travel.getId(), user.getId());
        travelService.addUserToTravel(travel.getId(), user1.getId());
        travelService.addUserToTravel(travel.getId(), user2.getId());
        Long scheduleId = scheduleService.createSchedule(
                new ScheduleCreateRequestDto(travel.getId(),
                        ajouUniv.getId(),
                        startTime,
                        endTime,
                        new ArrayList<>(List.of(user1.getId()))
                )
        );
        ScheduleUpdateRequestDto scheduleUpdateRequestDto = ScheduleUpdateRequestDto
                .builder()
                .scheduleId(scheduleId)
                .startTime(startTime)
                .endTime(endTime)
                .placeId(inhaUniv.getId())
                .userIds(new ArrayList<>(List.of(user.getId(), user2.getId())))
                .build();
        scheduleService.updateSchedule(scheduleUpdateRequestDto);
        ScheduleResponseDto schedule = scheduleService.getScheduleById(scheduleId);
        assertThat(schedule.getPlace().getPlaceId()).isEqualTo(inhaUniv.getId());
        assertThat(schedule.getUsers()).hasSize(2);
    }

    @Test
    @DisplayName("Schedule의 상세 정보를 뽑아올 수 있다.")
    @Rollback
    public void testFindSchedule() {
        User user1 = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(1L)
                        .build()
        );
        User user2 = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travel.getId(), user1.getId());
        travelService.addUserToTravel(travel.getId(), user2.getId());
        Long scheduleId = scheduleService.createSchedule(
                new ScheduleCreateRequestDto(
                        travel.getId(),
                        ajouUniv.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1),
                        new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId()))
                )
        );
        ScheduleResponseDto schedule = scheduleService.getScheduleById(scheduleId);
        assertThat(schedule.getPlace().getPlaceId()).isEqualTo(ajouUniv.getId());
        assertThat(schedule.getUsers()).hasSize(3);
    }
}