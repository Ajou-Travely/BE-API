//package com.ajou.travely.service;
//
//import com.ajou.travely.controller.cost.dto.CostCreateRequestDto;
//import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
//import com.ajou.travely.controller.place.dto.PlaceCreateRequestDto;
//import com.ajou.travely.controller.schedule.dto.ScheduleCreateRequestDto;
//import com.ajou.travely.controller.schedule.dto.SimpleScheduleResponseDto;
//import com.ajou.travely.controller.travel.dto.*;
//import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
//import com.ajou.travely.domain.Invitation;
//import com.ajou.travely.domain.UserTravel;
//import com.ajou.travely.domain.travel.Travel;
//import com.ajou.travely.domain.travel.TravelType;
//import com.ajou.travely.domain.user.UserType;
//import com.ajou.travely.domain.user.User;
//import com.ajou.travely.repository.InvitationRepository;
//import com.ajou.travely.repository.UserTravelRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.annotation.Rollback;
//
//import javax.transaction.Transactional;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
////TODO TravelService 가 수정됨에 따라 대대적으로 수정 밑 테스트 케이스 추가 필요
//@SpringBootTest(properties = {
//        "auth.kakaoOauth2ClinetId=test",
//        "auth.frontendRedirectUrl=test",
//        "spring.mail.password=temptemptemptemp"
//})
//@Transactional
//class TravelServiceTest {
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    TravelService travelService;
//
//    @Autowired
//    ScheduleService scheduleService;
//
//    @Autowired
//    CostService costService;
//
//    @Autowired
//    PlaceService placeService;
//
//    @Autowired
//    InvitationRepository invitationRepository;
//
//    @Autowired
//    InvitationService invitationService;
//
//    @Autowired
//    UserTravelRepository userTravelRepository;
//
//    @Value("${domain.base-url}")
//    private String baseUrl;
//
//    @Test
//    @DisplayName("여행 객체를 만들 수 있다.")
//    @Rollback
//    public void testCreateTravel() {
//        User user = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("sophoca@ajou.ac.kr")
//                        .name("홍성빈")
//                        .phoneNumber("112")
//                        .kakaoId(0L)
//                        .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//                .builder()
//                .title("test")
//                .userEmails(new ArrayList<>())
//                .build();
//
//        Travel travel = travelService.createTravel(user.getId(), request);
//
//        Long travelId = travel.getId();
//        TravelResponseDto foundTravel = travelService.getTravelById(travelId, user.getId());
//        assertThat(travelService.getAllTravels()).hasSize(1);
//        assertThat(foundTravel.getUsers()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("여행 객체를 업데이트 할 수 있다.")
//    @Rollback
//    public void testUpdateTravel() {
//        User user = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("sophoca@ajou.ac.kr")
//                        .name("홍성빈")
//                        .phoneNumber("112")
//                        .kakaoId(0L)
//                        .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//                .builder()
//                .title("test")
//                .userEmails(new ArrayList<>())
//                .build();
//        Travel travel = travelService.createTravel(user.getId(), request);
//        Long travelId = travel.getId();
//
//        String title = "updatedTitle";
//        String memo = "updatedMemo";
//        TravelUpdateRequestDto travelUpdateRequestDto = TravelUpdateRequestDto.builder()
//                .title(title)
//                .memo(memo)
//                .build();
//        travelService.updateTravel(travelId, user.getId(), travelUpdateRequestDto);
//
//        TravelResponseDto foundTravel = travelService.getTravelById(travelId, user.getId());
//        assertThat(foundTravel.getTitle()).isEqualTo(title);
//        assertThat(foundTravel.getMemo()).isEqualTo(memo);
//    }
//
//    @Test
//    @DisplayName("여행에 유저를 초대할 수 있다.")
//    @Rollback
//    public void testAddUserToTravel() {
//        User user = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("sophoca@ajou.ac.kr")
//                        .name("홍성빈")
//                        .phoneNumber("112")
//                        .kakaoId(0L)
//                        .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//                .builder()
//                .title("test")
//                .userEmails(new ArrayList<>())
//                .build();
//        Travel travel = travelService.createTravel(user.getId(), request);
//        Long travelId = travel.getId();
//        TravelResponseDto foundTravel = travelService.getTravelById(travelId, user.getId());
//        User newUser = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("errander@ajou.ac.kr")
//                        .name("이호용")
//                        .phoneNumber("119")
//                        .kakaoId(1L)
//                        .build()
//        );
//        travelService.addUserToTravel(
//                foundTravel.getId()
//                , newUser.getId()
//        );
//        List<SimpleUserInfoDto> users = travelService.getSimpleUsersInfoOfTravel(foundTravel.getId());
//        assertThat(users).hasSize(2);
//        users.forEach(u -> System.out.println(u.toString()));
//    }
//
//    @Test
//    @DisplayName("여행에 해당하는 지출 내역을 반환한다.")
//    public void testGetCostsByTravelId() {
//        List<Long> numbers = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));
//        List<User> users = new ArrayList<>();
//        numbers.forEach(number -> users.add(userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email(String.format("test%d@ajou.ac.kr", number))
//                        .name(String.format("test%d", number))
//                        .phoneNumber(String.format("11%d", number))
//                        .kakaoId(number)
//                        .build()
//        )));
//        TravelCreateRequestDto request = TravelCreateRequestDto
//                .builder()
//                .title("첫 여행")
//                .userEmails(new ArrayList<>())
//                .build();
//        Travel travel = travelService.createTravel(users.get(0).getId(), request);
//        Long travelId = travel.getId();
//        for (User user : users) {
//            travelService.addUserToTravel(travelId, user.getId());
//        }
//
//        Map<Long, Long> amountPerUser1 = new HashMap<>();
//        amountPerUser1.put(users.get(0).getId(), 1000L);
//        amountPerUser1.put(users.get(1).getId(), 10000L);
//        CostCreateRequestDto requestDto1 = CostCreateRequestDto.builder()
//                .totalAmount(11000L)
//                .title("TestTitle")
//                .content("안녕난이거야")
//                .amountsPerUser(amountPerUser1)
//                .payerId(users.get(0).getId())
//                .build();
//        CostCreateResponseDto createdCost1 = costService.createCost(
//                requestDto1, travelId
//        );
//
//        Map<Long, Long> amountPerUser2 = new HashMap<>();
//        amountPerUser2.put(users.get(2).getId(), 10000L);
//        amountPerUser2.put(users.get(3).getId(), 10000L);
//        CostCreateRequestDto requestDto2 = CostCreateRequestDto.builder()
//                .totalAmount(20000L)
//                .title("SecondTitle")
//                .content("안녕난그거야")
//                .amountsPerUser(amountPerUser2)
//                .payerId(users.get(2).getId())
//                .build();
//        CostCreateResponseDto createdCost2 = costService.createCost(
//                requestDto2, travelId
//        );
//        List<SimpleCostResponseDto> costsByTravelId = travelService.getCostsByTravelId(travelId, users.get(0).getId());
//
//        assertThat(costsByTravelId).hasSize(2);
//
//        assertThat(costsByTravelId.get(0).getTitle()).isEqualTo("TestTitle");
//        assertThat(costsByTravelId.get(0).getTotalAmount()).isEqualTo(11000L);
//        assertThat(costsByTravelId.get(0).getUserIds().toArray()).isEqualTo(Arrays.asList(users.get(0).getId(), users.get(1).getId()).toArray());
//
//        assertThat(costsByTravelId.get(1).getTitle()).isEqualTo("SecondTitle");
//        assertThat(costsByTravelId.get(1).getTotalAmount()).isEqualTo(20000L);
//        assertThat(costsByTravelId.get(1).getUserIds().toArray()).isEqualTo(Arrays.asList(users.get(2).getId(), users.get(3).getId()).toArray());
//    }
//
////    @Test
////    @DisplayName("여행의 schedule들을 불러올 수 있다.")
////    @Rollback
////    public void testGetSchedulesByTravel() {
////        PlaceCreateRequestDto ajouUniv = PlaceCreateRequestDto.builder()
////                .lat(4.5)
////                .lng(5.4)
////                .placeUrl("ajou.ac.kr")
////                .placeName("아주대학교")
////                .addressName("원천동")
////                .addressRoadName("원천로")
////                .kakaoMapId(1L)
////                .build();
////        PlaceCreateRequestDto inhaUniv = PlaceCreateRequestDto.builder()
////                .lat(3.7)
////                .lng(7.3)
////                .placeUrl("inha.ac.kr")
////                .placeName("인하대학교")
////                .addressName("인천")
////                .addressRoadName("인천로")
////                .phoneNumber("119")
////                .kakaoMapId(2L)
////                .build();
////        User user = userService.insertUser(
////                User.builder()
////                        .userType(UserType.USER)
////                        .email("sophoca@ajou.ac.kr")
////                        .name("홍성빈")
////                        .phoneNumber("112")
////                        .kakaoId(0L)
////                        .build()
////        );
////        TravelCreateRequestDto request = TravelCreateRequestDto
////                .builder()
////                .title("test")
////                .userEmails(new ArrayList<>())
////                .build();
////        Travel travel = travelService.createTravel(user.getId(), request);
////        Long travelId = travel.getId();
////        Long schedule1Id = scheduleService.createSchedule(
////                travelId,
////                ScheduleCreateRequestDto.builder()
////                        .place(ajouUniv)
////                        .startTime(LocalDateTime.now())
////                        .endTime(LocalDateTime.now().plusDays(1))
////                        .build());
////        Long schedule2Id = scheduleService.createSchedule(
////                travelId,
////                ScheduleCreateRequestDto.builder()
////                        .place(inhaUniv)
////                        .startTime(LocalDateTime.now().plusDays(1))
////                        .endTime(LocalDateTime.now().plusDays(2))
////                        .build());
////        List<SimpleScheduleResponseDto> schedules = travelService.getSchedulesByTravelId(travelId);
////        assertThat(schedules).hasSize(2);
////    }
//
//    @Test
//    void testPagination() {
//        // given
//        User user = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("sophoca@ajou.ac.kr")
//                        .name("홍성빈")
//                        .phoneNumber("112")
//                        .kakaoId(0L)
//                        .build()
//        );
//        for (int i = 0; i < 7; i++) {
//            TravelCreateRequestDto request = TravelCreateRequestDto
//                    .builder()
//                    .title("test" + i)
//                    .userEmails(new ArrayList<>())
//                    .build();
//            travelService.createTravel(user.getId(), request);
//        }
//
//        // when
//        PageRequest pageRequest0 = PageRequest.of(0, 3);
//        PageRequest pageRequest1 = PageRequest.of(1, 3);
//        PageRequest pageRequest2 = PageRequest.of(2, 3);
//        Page<SimpleTravelResponseDto> travels0 = userService.getTravelsByUser(user.getId(), pageRequest0);
//        Page<SimpleTravelResponseDto> travels1 = userService.getTravelsByUser(user.getId(), pageRequest1);
//        Page<SimpleTravelResponseDto> travels2 = userService.getTravelsByUser(user.getId(), pageRequest2);
//
//        // then
//        assertThat(travels0.getTotalElements()).isEqualTo(7);
//        assertThat(travels0.getTotalPages()).isEqualTo(3);
//
//        assertThat(travels0.isFirst()).isEqualTo(true);
//        assertThat(travels0.isLast()).isEqualTo(false);
//        assertThat(travels0.getSize()).isEqualTo(3);
//        assertThat(travels0.getNumberOfElements()).isEqualTo(3);
//        assertThat(travels0.getPageable().getPageNumber()).isEqualTo(0);
//
//        assertThat(travels1.getTotalElements()).isEqualTo(7);
//        assertThat(travels1.getTotalPages()).isEqualTo(3);
//
//        assertThat(travels1.isFirst()).isEqualTo(false);
//        assertThat(travels1.isLast()).isEqualTo(false);
//        assertThat(travels2.getSize()).isEqualTo(3);
//        assertThat(travels1.getNumberOfElements()).isEqualTo(3);
//        assertThat(travels1.getPageable().getPageNumber()).isEqualTo(1);
//
//        assertThat(travels2.getTotalElements()).isEqualTo(7);
//        assertThat(travels2.getTotalPages()).isEqualTo(3);
//
//        assertThat(travels2.isFirst()).isEqualTo(false);
//        assertThat(travels2.isLast()).isEqualTo(true);
//        assertThat(travels2.getSize()).isEqualTo(3);
//        assertThat(travels2.getNumberOfElements()).isEqualTo(1);
//        assertThat(travels2.getPageable().getPageNumber()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("여행 타입 기본값 입력")
//    @Rollback
//    public void testTravelTypeDefault() {
//        User user = userService.insertUser(
//            User.builder()
//                .userType(UserType.USER)
//                .email("sophoca@ajou.ac.kr")
//                .name("홍성빈")
//                .phoneNumber("112")
//                .kakaoId(0L)
//                .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//            .builder()
//            .title("test")
//            .userEmails(new ArrayList<>())
//            .build();
//        Travel travel = travelService.createTravel(user.getId(), request);
//        assertThat(travel.getTravelType()).isEqualTo(TravelType.PUBLIC);
//    }
//
//    @Test
//    @DisplayName("여행 타입 private 입력")
//    @Rollback
//    public void testTravelTypePrivate() {
//        User user = userService.insertUser(
//            User.builder()
//                .userType(UserType.USER)
//                .email("sophoca@ajou.ac.kr")
//                .name("홍성빈")
//                .phoneNumber("112")
//                .kakaoId(0L)
//                .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//            .builder()
//            .title("test")
//            .userEmails(new ArrayList<>())
//            .travelType(TravelType.PRIVATE)
//            .build();
//        Travel travel = travelService.createTravel(user.getId(), request);
//        assertThat(travel.getTravelType()).isEqualTo(TravelType.PRIVATE);
//    }
//
//    @Test
//    @DisplayName("여행 초대를 승락할 수 있다.")
//    void testAcceptInvitation() {
//        User user = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("sophoca@ajou.ac.kr")
//                        .name("홍성빈")
//                        .phoneNumber("112")
//                        .kakaoId(0L)
//                        .build()
//        );
//        User invitedUser = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("hooo0503@ajou.ac.kr")
//                        .name("박상혁")
//                        .phoneNumber("113")
//                        .kakaoId(1L)
//                        .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//                .builder()
//                .title("test")
//                .userEmails(new ArrayList<>())
//                .build();
//        Travel travel = travelService.createTravel(user.getId(), request);
//        UUID code = UUID.randomUUID();
//        Invitation invitation = invitationService.insertInvitation(
//                new Invitation(invitedUser.getEmail(), travel, code)
//        );
//
//        String redirectUri = travelService.acceptInvitation(invitedUser.getId(), code);
//        List<UserTravel> userTravelList = userTravelRepository.findAll();
//        Optional<Invitation> deletedInvitation = invitationRepository.findById(invitation.getId());
//
//        Assertions.assertThat(redirectUri).isEqualTo(baseUrl + travel.getId());
//        Assertions.assertThat(userTravelList.get(1).getUser().getId()).isEqualTo(invitedUser.getId());
//        Assertions.assertThat(userTravelList.get(1).getTravel().getId()).isEqualTo(travel.getId());
//        Assertions.assertThat(deletedInvitation.isEmpty()).isEqualTo(true);
//    }
//
//    @Test
//    @DisplayName("여행 초대를 거절할 수 있다.")
//    void testRejectInvitation() {
//        User user = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("sophoca@ajou.ac.kr")
//                        .name("홍성빈")
//                        .phoneNumber("112")
//                        .kakaoId(0L)
//                        .build()
//        );
//        User invitedUser = userService.insertUser(
//                User.builder()
//                        .userType(UserType.USER)
//                        .email("hooo0503@ajou.ac.kr")
//                        .name("박상혁")
//                        .phoneNumber("113")
//                        .kakaoId(1L)
//                        .build()
//        );
//        TravelCreateRequestDto request = TravelCreateRequestDto
//                .builder()
//                .title("test")
//                .userEmails(new ArrayList<>())
//                .build();
//        Travel travel = travelService.createTravel(user.getId(), request);
//        UUID code = UUID.randomUUID();
//        Invitation invitation = invitationService.insertInvitation(
//                new Invitation(invitedUser.getEmail(), travel, code)
//        );
//
//        travelService.rejectInvitation(invitedUser.getId(), code);
//        Optional<Invitation> foundInvitation = invitationRepository.findById(invitation.getId());
//
//        Assertions.assertThat(foundInvitation.isEmpty()).isEqualTo(true);
//    }
//}