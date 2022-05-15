package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleCostResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.controller.travel.dto.TravelResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

//TODO TravelService 가 수정됨에 따라 대대적으로 수정 밑 테스트 케이스 추가 필요
@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
})
@Transactional
class TravelServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    TravelService travelService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    CostService costService;

    @Autowired
    PlaceService placeService;

    @Test
    @DisplayName("여행 객체를 만들 수 있다.")
    @Rollback
    public void testCreateTravel() {
        User user = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(0L)
                        .build()
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
                .builder()
                .userId(user.getId())
                .title("test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();
        Long travelId = travelService.createTravel(user.getId(), request);
        TravelResponseDto foundTravel = travelService.getTravelById(travelId);
        assertThat(travelService.getAllTravels()).hasSize(1);
        assertThat(foundTravel.getUsers()).hasSize(1);
    }

    @Test
    @DisplayName("여행에 유저를 초대할 수 있다.")
    @Rollback
    public void testAddUserToTravel() {
        User user = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(0L)
                        .build()
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
                .builder()
                .userId(user.getId())
                .title("test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();
        Long travelId = travelService.createTravel(user.getId(), request);
        TravelResponseDto foundTravel = travelService.getTravelById(travelId);
        User newUser = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(1L)
                        .build()
        );
        travelService.addUserToTravel(
                foundTravel.getId()
                , newUser.getId()
        );
        List<SimpleUserInfoDto> users = travelService.getSimpleUsersOfTravel(foundTravel.getId());
        assertThat(users).hasSize(2);
        users.forEach(u -> System.out.println(u.toString()));
    }

    @Test
    @DisplayName("여행에 해당하는 지출 내역을 반환한다.")
    public void testGetCostsByTravelId() {
        List<Long> numbers = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));
        List<User> users = new ArrayList<>();
        numbers.forEach(number -> users.add(userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email(String.format("test%d@ajou.ac.kr", number))
                        .name(String.format("test%d", number))
                        .phoneNumber(String.format("11%d", number))
                        .kakaoId(number)
                        .build()
        )));
        TravelCreateRequestDto request = TravelCreateRequestDto
                .builder()
                .title("첫 여행")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .userId(users.get(0).getId())
                .build();
        Long travelId = travelService.createTravel(request.getUserId() ,request);
        for (User user : users) {
            travelService.addUserToTravel(travelId, user.getId());
        }

        Map<Long, Long> amountPerUser1 = new HashMap<>();
        amountPerUser1.put(users.get(0).getId(), 1000L);
        amountPerUser1.put(users.get(1).getId(), 10000L);

        CostCreateResponseDto createdCost1 = costService.createCost(
                11000L,
                travelId,
                "TestTitle",
                "안녕난이거야",
                false,
                amountPerUser1,
                users.get(0).getId()
        );

        Map<Long, Long> amountPerUser2 = new HashMap<>();
        amountPerUser2.put(users.get(2).getId(), 10000L);
        amountPerUser2.put(users.get(3).getId(), 10000L);

        CostCreateResponseDto createdCost2 = costService.createCost(
                20000L,
                travelId,
                "SecondTitle",
                "안녕난그거야",
                true,
                amountPerUser2,
                users.get(2).getId()
        );
        List<SimpleCostResponseDto> costsByTravelId = travelService.getCostsByTravelId(travelId);

        assertThat(costsByTravelId).hasSize(2);

        assertThat(costsByTravelId.get(0).getTitle()).isEqualTo("TestTitle");
        assertThat(costsByTravelId.get(0).getTotalAmount()).isEqualTo(11000L);
        assertThat(costsByTravelId.get(0).getUserIds().toArray()).isEqualTo(Arrays.asList(users.get(0).getId(), users.get(1).getId()).toArray());

        assertThat(costsByTravelId.get(1).getTitle()).isEqualTo("SecondTitle");
        assertThat(costsByTravelId.get(1).getTotalAmount()).isEqualTo(20000L);
        assertThat(costsByTravelId.get(1).getUserIds().toArray()).isEqualTo(Arrays.asList(users.get(2).getId(), users.get(3).getId()).toArray());
    }

    @Test
    @DisplayName("여행의 schedule들을 불러올 수 있다.")
    @Rollback
    public void testGetSchedulesByTravel() {
        PlaceCreateRequestDto ajouUniv = PlaceCreateRequestDto.builder()
                .x(4.5)
                .y(5.4)
                .placeUrl("ajou.ac.kr")
                .placeName("아주대학교")
                .addressName("원천동")
                .addressRoadName("원천로")
                .kakaoMapId(1L)
                .build();
        PlaceCreateRequestDto inhaUniv = PlaceCreateRequestDto.builder()
                .x(3.7)
                .y(7.3)
                .placeUrl("inha.ac.kr")
                .placeName("인하대학교")
                .addressName("인천")
                .addressRoadName("인천로")
                .phoneNumber("119")
                .kakaoMapId(2L)
                .build();
        User user = userService.insertUser(
                User.builder()
                        .type(Type.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(0L)
                        .build()
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
                .builder()
                .userId(user.getId())
                .title("test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();
        Long travelId = travelService.createTravel(user.getId(), request);
        TravelResponseDto travel = travelService.getTravelById(travelId);
        Long schedule1Id = scheduleService.createSchedule(
                travelId,
                ScheduleCreateRequestDto.builder()
                        .place(ajouUniv)
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusDays(1))
                        .build());
        Long schedule2Id = scheduleService.createSchedule(
                travelId,
                ScheduleCreateRequestDto.builder()
                        .place(inhaUniv)
                        .startTime(LocalDateTime.now().plusDays(1))
                        .endTime(LocalDateTime.now().plusDays(2))
                        .build());
        List<SimpleScheduleResponseDto> schedules = travelService.getSchedulesByTravelId(travelId);
        assertThat(schedules).hasSize(2);
    }

    //TODO 추후 구현할 테스트케이스
//    @Test
//    @DisplayName("여행을 업데이트 할 수 있다.")
//    public void update_travel() {
//        //Given
//        User user = userRepository.save(
//                User.builder()
//                        .type(Type.USER)
//                        .email("sophoca@ajou.ac.kr")
//                        .name("홍성빈")
//                        .phoneNumber("112")
//                        .kakaoId(0L)
//                        .build()
//        );
//        User user1 = userRepository.save(
//                User.builder()
//                        .type(Type.USER)
//                        .email("errander@ajou.ac.kr")
//                        .name("이호용")
//                        .phoneNumber("119")
//                        .kakaoId(1L)
//                        .build()
//        );
//        User user2 = userRepository.save(
//                User.builder()
//                        .type(Type.USER)
//                        .email("park@ajou.ac.kr")
//                        .name("박상혁")
//                        .phoneNumber("111")
//                        .kakaoId(2L)
//                        .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//                .builder()
//                .userId(user.getId())
//                .title("test")
//                .startDate(LocalDate.now())
//                .endDate(LocalDate.now().plusDays(1))
//                .build();
//        Long travelId = travelService.createTravel(user.getId(), request);
//        Travel travel = travelRepository.findTravelWithUsersById(travelId)
//                .orElseThrow(() -> new RecordNotFoundException(
//                        "해당 ID의 Travel이 존재하지 않습니다."
//                        , ErrorCode.TRAVEL_NOT_FOUND
//                ));
//        travelService.addUserToTravel(travelId, user1.getId());
//        travelService.addUserToTravel(travelId, user2.getId());
//        Place ajouUniv = placeService.insertPlace(
//                Place.builder()
//                        .x(4.5)
//                        .y(5.4)
//                        .placeUrl("ajou.ac.kr")
//                        .placeName("아주대학교")
//                        .addressName("원천동")
//                        .addressRoadName("원천로")
//                        .build());
//        Schedule schedule = Schedule.builder()
//                .travel(travel)
//                .startTime(LocalDateTime.now().plusDays(2))
//                .endTime(LocalDateTime.now().plusDays(3))
//                .place(ajouUniv)
//                .build();
//    }
}