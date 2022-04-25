package com.ajou.travely.domain.travel;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.UserTravel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import com.ajou.travely.repository.UserTravelRepository;
import java.time.LocalDate;
import java.util.List;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TravelRepositoryTest {

    @Autowired
    TravelRepository travelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTravelRepository userTravelRepository;

    @AfterEach
    public void cleanup() {
        userTravelRepository.deleteAll();
        userRepository.deleteAll();
        travelRepository.deleteAll();
    }

    private String title = "테스트 여행";
    private LocalDate now = LocalDate.now();
    private LocalDate startDate = now;
    private LocalDate endDate = now;
    private Long managerId = 1L;

    @DisplayName("Travel 생성 성공")
    @Test
    public void createTravelSuccess() {
        // given
        Travel travel = Travel.builder()
            .title(title)
            .managerId(managerId)
            .startDate(startDate)
            .endDate(endDate)
            .build();

        // when
        Travel result = travelRepository.save(travel);

        // then
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getManagerId()).isEqualTo(managerId);
        assertThat(result.getStartDate()).isEqualTo(LocalDate.now());
        assertThat(result.getEndDate()).isEqualTo(LocalDate.now());
    }

    @DisplayName("Travel 생성 실패")
    @Test
    public void createTravelFail() {
        // given
        Travel travel = Travel.builder()
            .build();

        // when
        assertThatThrownBy(()-> travelRepository.save(travel))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("Travel 전체 리스트 조회")
    @Test
    public void readAllTravelList() {
        // given
        Travel travel = Travel.builder()
            .title(title)
            .managerId(managerId)
            .startDate(startDate)
            .endDate(endDate)
            .build();

        User user = User.builder()
            .type(Type.USER)
            .email("user@test.com")
            .name("user1")
            .phoneNumber("010-1111-1111")
            .build();

        userRepository.save(user);
        travelRepository.save(travel);

        UserTravel userTravel = UserTravel.builder()
            .user(user)
            .travel(travel)
            .build();
        userTravelRepository.save(userTravel);

        // when
        List<Travel> result = travelRepository.findAll();

        // then
        Travel posts = result.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getManagerId()).isEqualTo(managerId);
        assertThat(posts.getStartDate()).isEqualTo(LocalDate.now());
        assertThat(posts.getEndDate()).isEqualTo(LocalDate.now());
    }

}
