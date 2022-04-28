package com.ajou.travely.service;

import com.ajou.travely.controller.travel.dto.TravelResponseDto;
import com.ajou.travely.controller.user.dto.SimpleUserInfoDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

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
        User user = userRepository.save(new User(Type.USER, "sophoca@ajou.ac.kr", "홍성빈", "112", 0L));
        TravelResponseDto travelResponseDto = travelService.insertTravel(Travel.builder().title("첫 여행").startDate(LocalDate.now()).endDate(LocalDate.now()).managerId(user.getId()).build());
        Assertions.assertThat(travelRepository.findAll()).hasSize(1);
        Assertions.assertThat(travelResponseDto.getUsers()).hasSize(1);
    }

    @Test
    @DisplayName("여행에 유저를 초대할 수 있다.")
    public void testAddUserToTravel() {
        User user = userRepository.save(new User(Type.USER, "sophoca@ajou.ac.kr", "홍성빈", "112", 0L));
        TravelResponseDto travelResponseDto = travelService.insertTravel(Travel.builder().title("첫 여행").startDate(LocalDate.now()).endDate(LocalDate.now()).managerId(user.getId()).build());

        User newUser = userRepository.save(new User(Type.USER, "errander@ajou.ac.kr", "이호용", "119", 0L));
        travelService.addUserToTravel(travelResponseDto.getId(), newUser.getId());
        List<SimpleUserInfoDto> users = travelService.getSimpleUsersOfTravel(travelResponseDto.getId());
        Assertions.assertThat(users).hasSize(2);
        users.forEach(u -> System.out.println(u.toString()));
    }
}