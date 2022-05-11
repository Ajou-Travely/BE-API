package com.ajou.travely.service;

import com.ajou.travely.controller.cost.dto.CostCreateResponseDto;
import com.ajou.travely.controller.travel.dto.SimpleCostResponseDto;
import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.CostRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDate;
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
    @Rollback
    public void testCreateTravel() {
        User user = userRepository.save(
                new User(
                        Type.USER
                        , "sophoca@ajou.ac.kr"
                        , "홍성빈"
                        , "112"
                        , 0L
                )
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
                .builder()
                .userId(user.getId())
                .title("test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();
        Long travelId = travelService.createTravel(user.getId(), request);
        Optional<Travel> foundTravel = travelRepository.findTravelWithUsersById(travelId);
        assertThat(foundTravel).isNotEmpty();
        assertThat(travelRepository.findAll()).hasSize(1);
        assertThat(foundTravel.get().getUserTravels()).hasSize(1);
    }

    @Test
    @DisplayName("여행에 유저를 초대할 수 있다.")
    @Rollback
    public void testAddUserToTravel() {
        User user = userRepository.save(
                new User(
                        Type.USER
                        , "sophoca1@ajou.ac.kr"
                        , "홍성빈"
                        , "112"
                        , 0L
                )
        );
        TravelCreateRequestDto request = TravelCreateRequestDto
                .builder()
                .userId(user.getId())
                .title("test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();
        Long travelId = travelService.createTravel(user.getId(), request);
        Optional<Travel> foundTravel = travelRepository.findById(travelId);
        User newUser = userRepository.save(
                new User(
                        Type.USER
                        , "errander@ajou.ac.kr"
                        , "이호용"
                        , "119"
                        , 0L
                )
        );
        travelService.addUserToTravel(
                foundTravel.get().getId()
                , newUser.getId()
        );
        List<SimpleUserInfoDto> users = travelService.getSimpleUsersOfTravel(foundTravel.get().getId());
        assertThat(users).hasSize(2);
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
}