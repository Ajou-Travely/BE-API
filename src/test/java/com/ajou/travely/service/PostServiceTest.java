package com.ajou.travely.service;

import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.repository.PlaceRepository;
import com.ajou.travely.repository.ScheduleRepository;
import com.ajou.travely.repository.TravelRepository;
import com.ajou.travely.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    TravelRepository travelRepository;

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    PlaceRepository placeRepository;

    public Travel travel;
    public Place place;
    public User user;
    public Schedule schedule;

    @BeforeEach
    public void setUp() {
        travel = travelRepository.save(Travel.builder()
            .title("place title")
            .managerId(1L)
            .memo("")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .build());
        place = placeRepository.save(Place.builder()
            .x(123.123)
            .y(345.1354)
            .placeName("place name")
            .addressName("주소1")
            .addressRoadName("주소2")
            .placeUrl("urlurl")
            .build());
        user = userRepository.save(User.builder()
            .type(Type.USER)
            .email("test@email.com")
            .name("user name")
            .phoneNumber("010-1111-1111")
            .kakaoId(12345152L)
            .build());
        schedule = scheduleRepository.save(Schedule.builder()
            .travel(travel)
            .place(place)
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now())
            .build());
    }

    @DisplayName("Post 생성")
    @Test
    void testCreatePost() {
        // given
        String title = "title";
        String text = "text";

        // when
        Post result = postService.createPost(user.getId(), schedule.getId(), title, text);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getSchedule().getId()).isEqualTo(schedule.getId());
        assertThat(result.getText()).isEqualTo(text);
        assertThat(result.getTitle()).isEqualTo(title);
    }

    @DisplayName("Post 수정")
    @Test
    void testUpdatePost() {
        // given
        String title = "title";
        String text = "text";
        Post post = postService.createPost(user.getId(), schedule.getId(), title, text);
        String updateTitle = "title2";
        String updateText = "text2";

        // when
        postService.updatePost(post.getId(), updateTitle, updateText);
        post = postService.findPostById(post.getId());

        // then
        assertThat(post.getText()).isEqualTo(updateText);
        assertThat(post.getTitle()).isEqualTo(updateTitle);

    }
}
