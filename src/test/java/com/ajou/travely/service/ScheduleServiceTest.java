package com.ajou.travely.service;

import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
import com.ajou.travely.controller.schedule.dto.ScheduleResponseDto;
import com.ajou.travely.controller.schedule.dto.ScheduleUpdateRequestDto;
import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
import com.ajou.travely.controller.schedulePhoto.dto.SchedulePhotoResponseDto;
import com.ajou.travely.controller.travel.dto.*;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.SchedulePhoto;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.travel.TravelDate;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.SchedulePhotoRepository;
import com.ajou.travely.repository.ScheduleRepository;
import com.ajou.travely.repository.TravelDateRepository;
import com.ajou.travely.repository.TravelRepository;

import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    TravelDateRepository travelDateRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    SchedulePhotoRepository schedulePhotoRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    TravelRepository travelRepository;

    PlaceCreateRequestDto ajouUniv;
    PlaceCreateRequestDto inhaUniv;
    User user;
    TravelResponseDto travelResponseDto;

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
                        .userType(UserType.USER)
                        .email("test@test")
                        .kakaoId(0L)
                        .build());
        TravelCreateRequestDto travelCreateRequestDto = TravelCreateRequestDto.builder()
                .title("첫 여행")
                .startDate(LocalDate.of(2022, 5, 10))
                .endDate(LocalDate.of(2022, 5, 15))
                .userEmails(new ArrayList<>())
                .build();
        travelResponseDto = travelService.createTravel(user.getId(), travelCreateRequestDto);
    }

    @Test
    @DisplayName("Schedule을 생성할 수 있다.")
    @Rollback
    public void testCreateSchedule() {
        User user1 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(1L)
                        .build()
        );
        User user2 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travelResponseDto.getId(), user1.getId());
        travelService.addUserToTravel(travelResponseDto.getId(), user2.getId());
        List<SimpleTravelDateResponseDto> travelDates = travelResponseDto.getDates();

        SimpleScheduleResponseDto schedule1 = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(2))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        SimpleScheduleResponseDto schedule2 = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(inhaUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(4))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        em.flush();
        em.clear();
        Travel foundTravel = travelRepository.findById(travelResponseDto.getId())
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 여행을 찾을 수 없습니다.",
                        ErrorCode.TRAVEL_NOT_FOUND
                ));
        ScheduleResponseDto schedule = scheduleService.getScheduleById(schedule1.getScheduleId());
        assertThat(schedule.getPlace().getPlaceName()).isEqualTo(ajouUniv.getPlaceName());
        assertThat(schedule.getUsers()).hasSize(3);
        assertThat(foundTravel.getTravelDates().size()).isEqualTo(6);
        assertThat(foundTravel.getTravelDates().get(0).getSchedules().size()).isEqualTo(2);
        assertThat(foundTravel.getTravelDates().get(0).getScheduleOrder().size()).isEqualTo(2);
        assertThat(foundTravel.getTravelDates().get(0).getScheduleOrder())
                .containsAll(Arrays.asList(schedule1.getScheduleId(),
                        schedule2.getScheduleId()));
    }

    @Test
    @DisplayName("Schedule을 업데이트할 수 있다.")
    @Rollback
    public void testUpdateSchedule() {
        LocalTime startTime = LocalTime.now();
        LocalTime endTime = LocalTime.now().plusHours(4);
        User user1 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(1L)
                        .build()
        );
        User user2 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travelResponseDto.getId(), user.getId());
        travelService.addUserToTravel(travelResponseDto.getId(), user1.getId());
        travelService.addUserToTravel(travelResponseDto.getId(), user2.getId());
        List<SimpleTravelDateResponseDto> travelDates = travelResponseDto.getDates();

        SimpleScheduleResponseDto schedule = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(4))
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
        scheduleService.updateSchedule(schedule.getScheduleId(), scheduleUpdateRequestDto);
        ScheduleResponseDto scheduleResponseDto = scheduleService.getScheduleById(schedule.getScheduleId());
        assertThat(scheduleResponseDto.getPlace().getPlaceName()).isEqualTo(inhaUniv.getPlaceName());
        assertThat(scheduleResponseDto.getUsers()).hasSize(2);
    }

    @Test
    @DisplayName("Schedule의 상세 정보를 뽑아올 수 있다.")
    @Rollback
    public void testFindSchedule() {
        User user1 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(1L)
                        .build()
        );
        User user2 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travelResponseDto.getId(), user1.getId());
        travelService.addUserToTravel(travelResponseDto.getId(), user2.getId());
        List<SimpleTravelDateResponseDto> travelDates = travelResponseDto.getDates();

        SimpleScheduleResponseDto schedule = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(4))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        ScheduleResponseDto scheduleResponseDto = scheduleService.getScheduleById(schedule.getScheduleId());
        assertThat(scheduleResponseDto.getPlace().getPlaceName()).isEqualTo(ajouUniv.getPlaceName());
        assertThat(scheduleResponseDto.getUsers()).hasSize(3);
    }

    @Test
    @DisplayName("생성한 schedule의 순서를 바꿀 수 있다.")
    void testChangeScheduleOrder() {
        User user1 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(1L)
                        .build()
        );
        User user2 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travelResponseDto.getId(), user1.getId());
        travelService.addUserToTravel(travelResponseDto.getId(), user2.getId());
        List<SimpleTravelDateResponseDto> travelDates = travelResponseDto.getDates();

        SimpleScheduleResponseDto schedule1 = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(2))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        SimpleScheduleResponseDto schedule2 = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(inhaUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(4))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        SimpleScheduleResponseDto schedule3 = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(4))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );
        em.flush();
        em.clear();
        travelService.changeScheduleOrder(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                new ScheduleOrderUpdateRequestDto(Arrays.asList(
                        schedule3.getScheduleId(),
                        schedule1.getScheduleId(),
                        schedule2.getScheduleId()))
        );
        List<SimpleScheduleResponseDto> schedulesByTravelId = travelService.getSchedulesByTravelIdAndDate(travelResponseDto.getId(), travelDates.get(0).getDate());
        List<Long> result = new ArrayList<>();
        schedulesByTravelId.forEach(simpleScheduleResponseDto -> result.add(simpleScheduleResponseDto.getScheduleId()));
        assertThat(result).isEqualTo(Arrays.asList(
                schedule3.getScheduleId(),
                schedule1.getScheduleId(),
                schedule2.getScheduleId())
        );
    }

    @Test
    @DisplayName("스케줄 기준 사진 조회")
    void testGetSchedulePhotos() {
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
        User user2 = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("errander@ajou.ac.kr")
                        .name("이호용")
                        .phoneNumber("119")
                        .kakaoId(2L)
                        .build()
        );
        travelService.addUserToTravel(travelResponseDto.getId(), user1.getId());
        travelService.addUserToTravel(travelResponseDto.getId(), user2.getId());

        List<SimpleTravelDateResponseDto> travelDates = travelResponseDto.getDates();

        SimpleScheduleResponseDto schedule = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(2))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId(), user2.getId())))
                        .build()
        );

        Schedule findSchedule = scheduleRepository.findById(schedule.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("no schedule"));

        // when
        List<String> photoPaths1 =
                new ArrayList<>(List.of("testUrl1", "testUrl2", "testUrl3"));
        List<SchedulePhoto> schedulePhotos1 = photoPaths1.stream()
                .map(photoPath ->
                        SchedulePhoto.builder()
                                .user(user1)
                                .schedule(findSchedule)
                                .photoPath(photoPath)
                                .build())
                .collect(Collectors.toList());
        schedulePhotoRepository.saveAll(schedulePhotos1);
        findSchedule.addSchedulePhotos(schedulePhotos1);

        List<String> photoPaths2 =
                new ArrayList<>(List.of("testUrl5", "testUrl6"));
        List<SchedulePhoto> schedulePhotos2 = photoPaths2.stream()
                .map(photoPath ->
                        SchedulePhoto.builder()
                                .user(user1)
                                .schedule(findSchedule)
                                .photoPath(photoPath)
                                .build())
                .collect(Collectors.toList());
        schedulePhotoRepository.saveAll(schedulePhotos2);
        findSchedule.addSchedulePhotos(schedulePhotos2);

        em.flush();
        em.clear();

        // then
        Schedule resultSchedule = scheduleRepository.findById(schedule.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("no schedule"));
        List<SchedulePhotoResponseDto> responseDtos =
                scheduleService.getSchedulePhotos(resultSchedule.getId());

        assertThat(responseDtos).hasSize(photoPaths1.size() + photoPaths2.size());
    }

    @Test
    @DisplayName("스케줄 사진들 삭제")
    void testDeleteSchedulePhotos() {
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
        travelService.addUserToTravel(travelResponseDto.getId(), user1.getId());

        List<SimpleTravelDateResponseDto> travelDates = travelResponseDto.getDates();

        SimpleScheduleResponseDto schedule = scheduleService.createSchedule(
                travelResponseDto.getId(),
                travelDates.get(0).getDate(),
                ScheduleCreateRequestDto
                        .builder()
                        .place(ajouUniv)
                        .startTime(LocalTime.now())
                        .endTime(LocalTime.now().plusHours(2))
                        .userIds(new ArrayList<>(List.of(user.getId(), user1.getId())))
                        .build()
        );

        Schedule findSchedule = scheduleRepository.findById(schedule.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("no schedule"));

        List<String> photoPaths =
                new ArrayList<>(List.of("testUrl1", "testUrl2", "testUrl3", "testUrl4", "testUrl5"));
        List<SchedulePhoto> schedulePhotos1 = photoPaths.stream()
                .map(photoPath ->
                        SchedulePhoto.builder()
                                .user(user1)
                                .schedule(findSchedule)
                                .photoPath(photoPath)
                                .build())
                .collect(Collectors.toList());
        schedulePhotoRepository.saveAll(schedulePhotos1);
        findSchedule.addSchedulePhotos(schedulePhotos1);

        List<Long> schedulePhotoIds =
                schedulePhotoRepository.findSchedulePhotosByScheduleIdInQuery(schedule.getScheduleId())
                        .stream()
                        .map(SchedulePhoto::getId)
                        .collect(Collectors.toList());
        System.out.println(schedulePhotoIds);

        // when
        List<Long> removeIds = List.of(schedulePhotoIds.get(0), schedulePhotoIds.get(1));
        scheduleService.deleteSchedulePhotos(schedule.getScheduleId(), removeIds);
        em.flush();
        em.clear();

        // then
        Schedule resultSchedule = scheduleRepository.findById(schedule.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("no schedule"));
        List<SchedulePhotoResponseDto> responseDtos =
                scheduleService.getSchedulePhotos(resultSchedule.getId());

        assertThat(responseDtos).hasSize(photoPaths.size() - removeIds.size());
    }

}