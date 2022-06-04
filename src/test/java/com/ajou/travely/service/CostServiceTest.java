package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.*;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.controller.travel.dto.TravelResponseDto;
import com.ajou.travely.domain.cost.Cost;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.repository.CostRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserCostRepository;
import com.ajou.travely.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest(properties = {
    "auth.kakaoOauth2ClinetId=test",
    "auth.frontendRedirectUrl=test",
    "spring.mail.password=temptemptemptemp"
})
@Transactional
class CostServiceTest {

    @Autowired
    TravelRepository travelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CostRepository costRepository;
    @Autowired
    UserCostRepository userCostRepository;
    @Autowired
    CostService costService;
    @PersistenceContext
    EntityManager em;
    @Autowired
    TravelService travelService;

    @Test
    @DisplayName("지출 객체를 생성할 수 있다.")
    @Rollback
    public void testCreateCost() {
        User user1 = userRepository.save(
            User.builder()
                .userType(UserType.USER)
                .email("test1@ajou.ac.kr")
                .name("테스트1")
                .phoneNumber("112")
                .kakaoId(0L)
                .build());
        User user2 = userRepository.save(
            User.builder()
                .userType(UserType.USER)
                .email("test2@ajou.ac.kr")
                .name("테스트2")
                .phoneNumber("119")
                .kakaoId(1L)
                .build());
        User user3 = userRepository.save(
            User.builder()
                .userType(UserType.USER)
                .email("test3@ajou.ac.kr")
                .name("테스트3")
                .phoneNumber("114")
                .kakaoId(2L)
                .build());
        Travel travel = travelRepository.save(
            Travel.builder()
                .title("첫 여행")
                .managerId(user1.getId())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(1))
                .build()
        );
        Map<Long, Long> amountPerUser = new HashMap<>();
        amountPerUser.put(user1.getId(), 10000L);
        amountPerUser.put(user2.getId(), 10000L);
        amountPerUser.put(user3.getId(), 10000L);
        CostCreateRequestDto requestDto = CostCreateRequestDto.builder()
                .totalAmount(30000L)
                .title("TestTitle")
                .content("안녕난이거야")
                .amountsPerUser(amountPerUser)
                .payerId(user1.getId()).build();
        CostCreateResponseDto costCreateResponseDto = costService.createCost(
            requestDto, travel.getId()
        );

        Assertions.assertThat(costCreateResponseDto.getTotalAmount()).isEqualTo(30000L);
        Assertions.assertThat(costCreateResponseDto.getTravel().getId()).isEqualTo(travel.getId());
        Assertions.assertThat(costCreateResponseDto.getPayer().getId()).isEqualTo(user1.getId());
        Assertions.assertThat(costCreateResponseDto.getUserCosts().size()).isEqualTo(amountPerUser.keySet().size());
    }

    @Test
    @DisplayName("지출 아이디를 통해 해당 지출을 가져올 수 있다.")
    @Rollback
    public void testGetCostById() {
        List<Long> numbers = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));
        List<User> users = new ArrayList<>();
        numbers.forEach(number ->
            users.add(userRepository.save(
                    User.builder()
                        .userType(UserType.USER)
                        .email(String.format("test%d@ajou.ac.kr", number))
                        .name(String.format("test%d", number))
                        .phoneNumber(String.format("11%d", number))
                        .kakaoId(number)
                        .build()
                )
            )
        );
        TravelResponseDto travelResponseDto = travelService.createTravel(users.get(0).getId(),
            TravelCreateRequestDto.builder()
                .title("첫 여행")
                .userEmails(new ArrayList<>())
                    .startDate(LocalDate.of(2022, 5, 10))
                    .endDate(LocalDate.of(2022, 5, 15))
                .build()
        );
        Long travelId = travelResponseDto.getId();
        for (User user : users) {
            travelService.addUserToTravel(travelId, user.getId());
        }

        Map<Long, Long> amountPerUser = new HashMap<>();
        amountPerUser.put(users.get(0).getId(), 1000L);
        amountPerUser.put(users.get(1).getId(), 10000L);
        amountPerUser.put(users.get(2).getId(), 100000L);
        CostCreateRequestDto requestDto = CostCreateRequestDto.builder()
                .totalAmount(111000L)
                .title("TestTitle")
                .content("안녕난이거야")
                .amountsPerUser(amountPerUser)
                .payerId(users.get(0).getId())
                .build();
        CostCreateResponseDto createdCost = costService.createCost(
            requestDto, travelId
        );
        CostResponseDto costById = costService.getCostById(createdCost.getId());

        Assertions.assertThat(costById.getCostId()).isEqualTo(createdCost.getId());
        Assertions.assertThat(costById.getContent()).isEqualTo(createdCost.getContent());
        Assertions.assertThat(costById.getPayerId()).isEqualTo(createdCost.getPayer().getId());
        Assertions.assertThat(
                costById.getUserCosts()
                    .stream()
                    .map(UserCostResponseDto::getUserCostId)
                    .toArray())
            .isEqualTo(
                createdCost.getUserCosts()
                    .stream()
                    .map(UserCost::getId)
                    .toArray());
        Assertions.assertThat(
                costById.getUserCosts()
                    .stream()
                    .map(userCostResponseDto ->
                        Arrays.asList(
                            userCostResponseDto.getSimpleUserInfoDto().getUserId(),
                            userCostResponseDto.getSimpleUserInfoDto().getUserName()))
                    .toArray())
            .isEqualTo(
                createdCost.getUserCosts()
                    .stream()
                    .map(userCost ->
                        Arrays.asList(userCost.getUser().getId(), userCost.getUser().getName()))
                    .toArray());
    }

    @Test
    @DisplayName("지출 정보를 수정할 수 있다.")
    @Rollback
    void testUpdateCostById() {
        List<Long> numbers = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L));
        List<User> users = new ArrayList<>();
        numbers.forEach(number ->
            users.add(userRepository.save(
                User.builder()
                    .userType(UserType.USER)
                    .email(String.format("test%d@ajou.ac.kr", number))
                    .name(String.format("test%d", number))
                    .phoneNumber(String.format("11%d", number))
                    .kakaoId(number)
                    .build()
            ))
        );
        Travel travel = travelService.insertTravel(
            Travel.builder()
                .title("첫 여행")
                .managerId(users.get(0).getId())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(1))
                .build()
        );
        for (User user : users) {
            travelService.addUserToTravel(travel.getId(), user.getId());
        }

        Map<Long, Long> amountPerUser = new HashMap<>();
        amountPerUser.put(users.get(0).getId(), 1000L);
        amountPerUser.put(users.get(1).getId(), 10000L);
        amountPerUser.put(users.get(2).getId(), 100000L);
        amountPerUser.put(users.get(3).getId(), 1000000L);
        CostCreateRequestDto requestDto = CostCreateRequestDto.builder()
                .totalAmount(1111000L)
                .title("TestTitle")
                .content("안녕난이거야")
                .amountsPerUser(amountPerUser)
                .payerId(users.get(0).getId())
                .build();
        CostCreateResponseDto createdCost = costService.createCost(
            requestDto, travel.getId()
        );

        Cost cost = costRepository.findById(createdCost.getId())
            .orElseThrow(() -> new RuntimeException("지출 없다."));
        String changedTitle = "ChangedTitle";
        String changedContent = "ChangedContent";
        Map<Long, CostUpdateInfoDto> amountsPerUser = new HashMap<>();
        amountsPerUser.put(users.get(2).getId(), new CostUpdateInfoDto(20000L, true));
        amountsPerUser.put(users.get(3).getId(), new CostUpdateInfoDto(2000L, true));
        amountsPerUser.put(users.get(4).getId(), new CostUpdateInfoDto(100000L, false));
        amountsPerUser.put(users.get(5).getId(), new CostUpdateInfoDto(1000000L, false));
        CostUpdateDto costUpdateDto = new CostUpdateDto(
            1122000L,
            changedTitle,
            changedContent,
            amountsPerUser,
            users.get(2).getId()
        );
        costService.updateCostById(cost.getId(), costUpdateDto);
        em.clear();
        Cost updatedCost = costRepository.getCostById(cost.getId())
            .orElseThrow(() -> new RuntimeException("없어"));

        Assertions.assertThat(updatedCost.getContent()).isEqualTo(changedContent);
        Assertions.assertThat(updatedCost.getTitle()).isEqualTo(changedTitle);
        Assertions.assertThat(updatedCost.getPayerId()).isEqualTo(users.get(2).getId());
        Assertions.assertThat(
            updatedCost.getUserCosts().stream().map(userCost -> userCost.getUser().getId())
                .toArray()).isEqualTo(amountsPerUser.keySet().toArray());
        Assertions.assertThat(
            updatedCost.getUserCosts().stream().map(UserCost::getAmount).toArray()).isEqualTo(
            amountsPerUser.keySet().stream().map(userId -> amountsPerUser.get(userId).getAmount())
                .toArray());
        Assertions.assertThat(
            updatedCost.getUserCosts().stream().map(UserCost::getIsRequested).toArray()).isEqualTo(
            amountsPerUser.keySet().stream()
                .map(userId -> amountsPerUser.get(userId).getIsRequested()).toArray());
    }

    @Test
    @DisplayName("지출 객체를 삭제할 수 있다.")
    @Rollback
    void testDeleteCostById() {
        User user1 = userRepository.save(
            User.builder()
                .userType(UserType.USER)
                .email("test1@ajou.ac.kr")
                .name("테스트1")
                .phoneNumber("112")
                .kakaoId(0L)
                .build());
        User user2 = userRepository.save(
            User.builder()
                .userType(UserType.USER)
                .email("test2@ajou.ac.kr")
                .name("테스트2")
                .phoneNumber("119")
                .kakaoId(1L)
                .build());
        User user3 = userRepository.save(
            User.builder()
                .userType(UserType.USER)
                .email("test3@ajou.ac.kr")
                .name("테스트3")
                .phoneNumber("114")
                .kakaoId(2L)
                .build());
        Travel travel = travelService.insertTravel(
            Travel.builder()
                .title("첫 여행")
                .managerId(user1.getId())
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(1))
                .build()
        );
        Map<Long, Long> amountPerUser = new HashMap<>();
        amountPerUser.put(user1.getId(), 10000L);
        amountPerUser.put(user2.getId(), 10000L);
        amountPerUser.put(user3.getId(), 10000L);
        CostCreateRequestDto requestDto = CostCreateRequestDto.builder()
                .totalAmount(30000L)
                .title("TestTitle")
                .content("안녕난이거야")
                .amountsPerUser(amountPerUser)
                .payerId(user1.getId())
                .build();
        CostCreateResponseDto costCreateResponseDto = costService.createCost(
            requestDto, travel.getId()
        );
        CostResponseDto foundCost = costService.getCostById(costCreateResponseDto.getId());
        Stream<Long> userCostIds = foundCost.getUserCosts().stream()
            .map(UserCostResponseDto::getUserCostId);
        costService.deleteCostById(costCreateResponseDto.getId());

        Assertions.assertThat(userCostIds.map(userCostId ->
                    userCostRepository.findById(userCostId).isEmpty())
                .collect(Collectors.toList()))
            .isEqualTo(Arrays.asList(true, true, true));
    }
}