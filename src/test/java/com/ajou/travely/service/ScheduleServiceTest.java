package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
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
        "spring.mail.password=temptemptemptemp"
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

    PlaceCreateRequestDto ajouUniv;
    PlaceCreateRequestDto inhaUniv;
    User user;
    Travel travel;

    @BeforeEach
    public void setUp() {
        ajouUniv = PlaceCreateRequestDto.builder()
                .lat(4.5)
                .lng(5.4)
                .placeUrl("ajou.ac.kr")
                .placeName("아주대학교")
                .addressName("원천동")
                .addressRoadName("원천로")
                .kakaoMapId(1L)
                .build();
        inhaUniv = PlaceCreateRequestDto.builder()
                .lat(3.7)
                .lng(7.3)
                .placeUrl("inha.ac.kr")
                .placeName("인하대학교")
                .addressName("인천")
                .addressRoadName("인천로")
                .phoneNumber("119")
                .kakaoMapId(2L)
                .build();
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
                travel.getId(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusDays(1))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        ScheduleResponseDto schedule = scheduleService.getScheduleById(scheduleId);
        assertThat(schedule.getPlace().getPlaceName()).isEqualTo(ajouUniv.getPlaceName());
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
                travel.getId(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusDays(1))
                        .userIds(new ArrayList<>(List.of(user1.getId())))
                        .build()
        );
        ScheduleUpdateRequestDto scheduleUpdateRequestDto = ScheduleUpdateRequestDto
                .builder()
                .startTime(startTime)
                .endTime(endTime)
                .place(inhaUniv)
                .userIds(new ArrayList<>(List.of(user.getId(), user2.getId())))
                .build();
        scheduleService.updateSchedule(scheduleId, scheduleUpdateRequestDto);
        ScheduleResponseDto schedule = scheduleService.getScheduleById(scheduleId);
        assertThat(schedule.getPlace().getPlaceName()).isEqualTo(inhaUniv.getPlaceName());
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
                travel.getId(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusDays(1))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        ScheduleResponseDto schedule = scheduleService.getScheduleById(scheduleId);
        assertThat(schedule.getPlace().getPlaceName()).isEqualTo(ajouUniv.getPlaceName());
        assertThat(schedule.getUsers()).hasSize(3);
    }
}