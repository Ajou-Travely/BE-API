package com.ajou.travely.service;

import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.UserTravelRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScheduleServiceTest {
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    TravelService travelService;

    @Autowired
    PlaceService placeService;

    @Autowired
    UserService userService;

    @Autowired
    UserTravelRepository userTravelRepository;

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
                        .phoneNumber("119")
                        .type(Type.USER)
                        .email("test@test")
                        .kakaoId(1L)
                        .build());
        travel = travelService.insertTravel(
                Travel.builder()
                        .managerId(user.getId())
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .title("첫 여행")
                        .build());
    }

    @AfterEach
    public void cleanUp() {
        scheduleService.deleteAllSchedules();
        userTravelRepository.deleteAll();
        travelService.deleteAllTravels();
        userService.deleteAllUsers();
        placeService.deleteAllPlaces();
    }

    @Test
    @DisplayName("생성한 Schedule을 DB에 삽입할 수 있다.")
    public void testCreateSchedule() {
        Schedule schedule = scheduleService.createSchedule(
                travel.getId(),
                ajouUniv.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        assertThat(schedule.getPlace().getId(), is(ajouUniv.getId()));
        assertThat(schedule.getTravel().getId(), is(travel.getId()));
        assertThat(schedule.getId(), is(schedule.getId()));
    }

    @Test
    @DisplayName("여행의 schedule들을 불러올 수 있다.")
    public void testGetSchedulesByTravel() {
        Schedule schedule1 = scheduleService.insertSchedule(
                Schedule.builder()
                        .travel(travel)
                        .place(ajouUniv)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusDays(1))
                        .build());
        Schedule schedule2 = scheduleService.insertSchedule(
                Schedule.builder()
                        .travel(travel)
                        .place(inhaUniv)
                        .startTime(LocalDateTime.now().plusDays(1))
                        .endTime(LocalDateTime.now().plusDays(2))
                        .build());

        List<Schedule> schedules = scheduleService.getSchedulesByTravelId(travel.getId());

//        schedules.forEach(s -> System.out.println(s.getPlace().getPlaceName()));
//        Lazy 에러 발생

        assertThat(schedules, hasSize(2));
    }

    @Test
    @DisplayName("여행의 schedule들을 place와 함께 불러올 수 있다.")
    public void testGetSchedulesWithPlaceByTravel() {
        Schedule schedule1 = scheduleService.insertSchedule(
                Schedule.builder()
                        .travel(travel)
                        .place(ajouUniv)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusDays(1))
                        .build());
        Schedule schedule2 = scheduleService.insertSchedule(
                Schedule.builder()
                        .travel(travel)
                        .place(inhaUniv)
                        .startTime(LocalDateTime.now().plusDays(1))
                        .endTime(LocalDateTime.now().plusDays(2))
                        .build());

        List<Schedule> schedules = scheduleService.getSchedulesWithPlaceByTravelId(travel.getId());

        schedules.forEach(s -> System.out.println(s.getPlace().getPlaceName()));

        assertThat(schedules, hasSize(2));
    }
}