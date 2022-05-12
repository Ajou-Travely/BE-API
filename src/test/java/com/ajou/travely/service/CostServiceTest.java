package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.cost.dto.CostResponseDto;
import com.ajou.travely.controller.cost.dto.UserCostResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserCost;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
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
    CostService costService;
    @Autowired
    TravelService travelService;

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
        Travel travel = travelRepository.save(
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
        numbers.forEach(number -> users.add(userRepository.save(new User(
                Type.USER,
                String.format("test%d@ajou.ac.kr", number),
                String.format("test%d", number),
                String.format("11%d", number),
                number
        ))));
        Long travelId = travelService.createTravel(users.get(0).getId(),
                TravelCreateRequestDto.builder()
                        .title("첫 여행")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now())
                        .userId(users.get(0).getId())
                        .build()
        );
        for (User user : users) {
            travelService.addUserToTravel(travelId, user.getId());
        }

        Map<Long, Long> amountPerUser = new HashMap<>();
        amountPerUser.put(users.get(0).getId(), 1000L);
        amountPerUser.put(users.get(1).getId(), 10000L);
        amountPerUser.put(users.get(2).getId(), 100000L);

        CostCreateResponseDto createdCost = costService.createCost(
                111000L,
                travelId,
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
        Assertions.assertThat(costById.getUserCostResponseDtos().stream().map(UserCostResponseDto::getUserCostId).toArray()).isEqualTo(createdCost.getUserCosts().stream().map(UserCost::getId).toArray());
        Assertions.assertThat(costById.getUserCostResponseDtos().stream().map(userCostResponseDto -> Arrays.asList(userCostResponseDto.getSimpleUserInfoDto().getUserId(), userCostResponseDto.getSimpleUserInfoDto().getUserName())).toArray()).isEqualTo(createdCost.getUserCosts().stream().map(userCost -> Arrays.asList(userCost.getUser().getId(), userCost.getUser().getName())).toArray());
    }
}