package com.ajou.travely.domain.travel;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ajou.travely.domain.Travel;
import com.ajou.travely.repository.TravelRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TravelRepositoryTest {

    @Autowired
    TravelRepository travelRepository;

    @AfterEach
    public void cleanup() {
        travelRepository.deleteAll();
    }

    @DisplayName("Travel 전체 리스트 조회")
    @Test
    public void readAllTravelList() {
        // given
        String title = "테스트 여행";
        String memo = "테스트 메모";
        LocalDate now = LocalDate.of(2022,4,18);
        LocalDate startDate = now;
        LocalDate endDate = now;

        travelRepository.save(Travel.builder()
            .title(title)
            .memo(memo)
            .startDate(startDate)
            .endDate(endDate)
            .build()
        );

        // when
        List<Travel> travelList = travelRepository.findAll();

        // then
        Travel posts = travelList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getMemo()).isEqualTo(memo);
        assertThat(posts.getStartDate()).isBefore(LocalDate.now());
        assertThat(posts.getEndDate()).isBefore(LocalDate.now());
    }
}
