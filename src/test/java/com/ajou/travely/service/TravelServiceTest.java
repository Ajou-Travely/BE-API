package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.TravelCreateRequestDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//TODO TravelService 가 수정됨에 따라 대대적으로 수정 밑 테스트 케이스 추가 필요
@SpringBootTest
class TravelServiceTest {
    @Autowired
    TravelRepository travelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TravelService travelService;

    @Test
    @DisplayName("여행 객체를 만들 수 있다.")
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
}