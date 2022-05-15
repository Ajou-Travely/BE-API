package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.cost.dto.CostUpdateDto;
import com.ajou.travely.controller.cost.dto.CostUpdateInfoDto;
import com.ajou.travely.domain.Cost;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.CostRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserCostRepository;
import com.ajou.travely.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.*;


@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
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
    TravelService travelService;
    @Autowired
    CostService costService;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("지출 객체를 생성할 수 있다.")
    @Rollback
    public void testCreateCost() {
        User user1 = userRepository.save(
                new User(
                        Type.USER,
                        "test1@ajou.ac.kr",
                        "테스트1",
                        "112",
                        0L
                ));
        User user2 = userRepository.save(
                new User(
                        Type.USER,
                        "test2@ajou.ac.kr",
                        "테스트2",
                        "113",
                        1L
                ));
        User user3 = userRepository.save(
                new User(
                        Type.USER,
                        "test3@ajou.ac.kr",
                        "테스트3",
                        "114",
                        2L
                ));
        Travel travel = travelService.insertTravel(
                Travel.builder()
                        .title("첫 여행")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .managerId(user1.getId())
                        .build()
        );
        Map<Long, Long> amountPerUser = new HashMap<>();
        amountPerUser.put(user1.getId(), 10000L);
        amountPerUser.put(user2.getId(), 10000L);
        amountPerUser.put(user3.getId(), 10000L);
        CostCreateResponseDto costCreateResponseDto = costService.createCost(
                30000L,
                travel.getId(),
                "TestTitle",
                "안녕난이거야",
                false,
                amountPerUser,
                user1.getId()
        );

        Assertions.assertThat(costCreateResponseDto.getTotalAmount()).isEqualTo(30000L);
        Assertions.assertThat(costCreateResponseDto.getTravel().getId()).isEqualTo(travel.getId());
        Assertions.assertThat(costCreateResponseDto.getPayer().getId()).isEqualTo(user1.getId());
        Assertions.assertThat(costCreateResponseDto.getUserCosts().get(0).getUser().getKakaoId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("지출 아이디를 통해 해당 지출을 가져올 수 있다.")
    @Rollback
    public void testGetCostById() {
        List<Long> numbers = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));
        List<User> users = new ArrayList<>();
        numbers.forEach(number -> {
            users.add(userRepository.save(new User(
                    Type.USER,
                    String.format("test%d@ajou.ac.kr", number),
                    String.format("test%d", number),
                    String.format("11%d", number),
                    number
            )));
        });
        Travel travel = travelService.insertTravel(
                Travel.builder()
                        .title("첫 여행")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .managerId(users.get(0).getId())
                        .build()
        );
        for (User user : users) {
            travelService.addUserToTravel(travel.getId(), user.getId());
        }

        Map<Long, Long> amountPerUser = new HashMap<>();
        amountPerUser.put(users.get(0).getId(), 1000L);
        amountPerUser.put(users.get(1).getId(), 10000L);
        amountPerUser.put(users.get(2).getId(), 100000L);

        CostCreateResponseDto createdCost = costService.createCost(
                111000L,
                travel.getId(),
                "TestTitle",
                "안녕난이거야",
                false,
                amountPerUser,
                users.get(0).getId()
        );
        CostResponseDto costById = costService.getCostById(createdCost.getId());

        Assertions.assertThat(costById.getCostId()).isEqualTo(createdCost.getId());
        Assertions.assertThat(costById.getContent()).isEqualTo(createdCost.getContent());
        Assertions.assertThat(costById.getPayerId()).isEqualTo(createdCost.getPayer().getId());
        Assertions.assertThat(costById.getUserCostResponseDtos().stream().map(userCostResponseDto -> {
            return userCostResponseDto.getUserCostId();
        }).toArray()).isEqualTo(createdCost.getUserCosts().stream().map(userCost -> {
            return userCost.getId();
        }).toArray());
        Assertions.assertThat(costById.getUserCostResponseDtos().stream().map(userCostResponseDto -> {
            return Arrays.asList(userCostResponseDto.getSimpleUserInfoDto().getUserId(), userCostResponseDto.getSimpleUserInfoDto().getUserName());
        }).toArray()).isEqualTo(createdCost.getUserCosts().stream().map(userCost -> {
            return Arrays.asList(userCost.getUser().getId(), userCost.getUser().getName());
        }).toArray());
    }

    @Test
    @DisplayName("지출 정보를 수정할 수 있다.")
    @Rollback
    void testUpdateCostById() {
        List<Long> numbers = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L));
        List<User> users = new ArrayList<>();
        numbers.forEach(number -> {
            users.add(userRepository.save(new User(
                    Type.USER,
                    String.format("test%d@ajou.ac.kr", number),
                    String.format("test%d", number),
                    String.format("11%d", number),
                    number
            )));
        });
        Travel travel = travelService.insertTravel(
                Travel.builder()
                        .title("첫 여행")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .managerId(users.get(0).getId())
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

        CostCreateResponseDto createdCost = costService.createCost(
                1111000L,
                travel.getId(),
                "TestTitle",
                "안녕난이거야",
                false,
                amountPerUser,
                users.get(0).getId()
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
                false,
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
        Assertions.assertThat(updatedCost.getUserCosts().stream().map(userCost -> userCost.getUser().getId()).toArray()).isEqualTo(amountsPerUser.keySet().stream().toArray());
        Assertions.assertThat(updatedCost.getUserCosts().stream().map(userCost -> userCost.getAmount()).toArray()).isEqualTo(amountsPerUser.keySet().stream().map(userId -> amountsPerUser.get(userId).getAmount()).toArray());
        Assertions.assertThat(updatedCost.getUserCosts().stream().map(userCost -> userCost.getIsRequested()).toArray()).isEqualTo(amountsPerUser.keySet().stream().map(userId -> amountsPerUser.get(userId).getIsRequested()).toArray());
    }
}