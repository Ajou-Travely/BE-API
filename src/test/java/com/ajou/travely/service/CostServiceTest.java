package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostResponseDto;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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

    @Test
    @DisplayName("지출 객체를 생성할 수 있다.")
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
        CostResponseDto costResponseDto = costService.createCost(
                30000L,
                travel.getId(),
                "TestTitle",
                "안녕난이거야",
                false,
                amountPerUser,
                user1.getId()
        );

        Assertions.assertThat(costResponseDto.getTotalAmount()).isEqualTo(30000L);
        Assertions.assertThat(costResponseDto.getTravel().getId()).isEqualTo(travel.getId());
        Assertions.assertThat(costResponseDto.getPayer().getId()).isEqualTo(user1.getId());
        Assertions.assertThat(costResponseDto.getUserCosts().get(0).getUser().getKakaoId()).isEqualTo(0L);
    }


}