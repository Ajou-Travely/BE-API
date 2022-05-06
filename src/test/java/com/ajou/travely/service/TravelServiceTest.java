package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.travel.dto.CostsResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.CostRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
class TravelServiceTest {
    @Autowired
    TravelRepository travelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TravelService travelService;

    @Autowired
    CostRepository costRepository;

    @Autowired
    CostService costService;

    @Test
    @DisplayName("여행 객체를 만들 수 있다.")
    public void testCreateTravel() {
        User user = userRepository.save(new User(Type.USER, "sophoca@ajou.ac.kr", "홍성빈", "112", 0L));
        Travel travel = travelService.insertTravel(Travel.builder().title("첫 여행").startDate(LocalDate.now()).endDate(LocalDate.now()).managerId(user.getId()).build());
        Assertions.assertThat(travelRepository.findAll()).hasSize(1);
        Assertions.assertThat(travel.getUserTravels()).hasSize(1);
    }

    @Test
    @DisplayName("여행에 유저를 초대할 수 있다.")
    public void testAddUserToTravel() {
        User user = userRepository.save(new User(Type.USER, "sophoca1@ajou.ac.kr", "홍성빈", "112", 0L));
        Travel travel = travelService.insertTravel(Travel.builder().title("첫 여행").startDate(LocalDate.now()).endDate(LocalDate.now()).managerId(user.getId()).build());

        User newUser = userRepository.save(new User(Type.USER, "errander@ajou.ac.kr", "이호용", "119", 0L));
        travelService.addUserToTravel(travel.getId(), newUser.getId());
        List<SimpleUserInfoDto> users = travelService.getSimpleUsersOfTravel(travel.getId());
        Assertions.assertThat(users).hasSize(2);
        users.forEach(u -> System.out.println(u.toString()));
    }

    @Test
    @DisplayName("여행에 해당하는 지출 내역을 반환한다.")
    public void testGetCostsByTravelId() {
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

        Map<Long, Long> amountPerUser1 = new HashMap<>();
        amountPerUser1.put(users.get(0).getId(), 1000L);
        amountPerUser1.put(users.get(1).getId(), 10000L);

        CostCreateResponseDto createdCost1 = costService.createCost(
                11000L,
                travel.getId(),
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
                travel.getId(),
                "SecondTitle",
                "안녕난그거야",
                true,
                amountPerUser2,
                users.get(2).getId()
        );
        List<CostsResponseDto> costsByTravelId = travelService.getCostsByTravelId(travel.getId());

        for (CostsResponseDto costsResponseDto : costsByTravelId) {
            System.out.println("costsResponseDto.getCostId() = " + costsResponseDto.getCostId());
        }
        Assertions.assertThat(costsByTravelId).hasSize(2);

        Assertions.assertThat(costsByTravelId.get(0).getTitle()).isEqualTo("TestTitle");
        Assertions.assertThat(costsByTravelId.get(0).getTotalAmount()).isEqualTo(11000L);
        Assertions.assertThat(costsByTravelId.get(0).getUserInfo().keySet().stream().toArray()).isEqualTo(Arrays.asList(users.get(0).getId(), users.get(1).getId()).toArray());
        Assertions.assertThat(costsByTravelId.get(0).getUserInfo().values().toArray()).isEqualTo(Arrays.asList(users.get(0).getName(), users.get(1).getName()).toArray());

        Assertions.assertThat(costsByTravelId.get(1).getTitle()).isEqualTo("SecondTitle");
        Assertions.assertThat(costsByTravelId.get(1).getTotalAmount()).isEqualTo(20000L);
        Assertions.assertThat(costsByTravelId.get(1).getUserInfo().keySet().stream().toArray()).isEqualTo(Arrays.asList(users.get(2).getId(), users.get(3).getId()).toArray());
        Assertions.assertThat(costsByTravelId.get(1).getUserInfo().values().toArray()).isEqualTo(Arrays.asList(users.get(2).getName(), users.get(3).getName()).toArray());
    }
}