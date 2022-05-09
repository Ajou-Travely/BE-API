package com.ajou.travely.service;

import com.ajou.travely.controller.post.dto.PostCreateRequestDto;
import com.ajou.travely.controller.post.dto.PostUpdateRequestDto;
import com.ajou.travely.domain.Photo;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<String> photoPaths = new ArrayList<>(Arrays.asList("link1", "link2"));

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
        Schedule schedule = scheduleRepository.findAll().get(0);
        PostCreateRequestDto requestDto = new PostCreateRequestDto(schedule.getId(), title, text, photoPaths);

        // when
        Post result = postService.findPostById(postService.createPost(user.getId(), requestDto));

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getSchedule().getId()).isEqualTo(schedule.getId());
        assertThat(result.getText()).isEqualTo(text);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getPhotos()).hasSize(2);
    }

    @DisplayName("Post 수정")
    @Test
    void testUpdatePost() {
        // given
        String title = "title";
        String text = "text";
        Schedule schedule = scheduleRepository.findAll().get(0);
        PostCreateRequestDto requestDto = new PostCreateRequestDto(schedule.getId(), title, text, photoPaths);
        Long postId = postService.createPost(user.getId(), requestDto);
        Post post = postService.initializePostInfo(postId);

        // when
        String updateTitle = "title2";
        String updateText = "text2";
        List<String> addedPhotoPaths = new ArrayList<>(Arrays.asList("link3", "link4", "link5"));
        List<Long> removedPhotoIds = new ArrayList<>(Arrays.asList(1L, 2L));
        PostUpdateRequestDto updateRequestDto =
            new PostUpdateRequestDto(updateTitle, updateText, addedPhotoPaths, removedPhotoIds);
        postService.updatePost(postId, updateRequestDto);
        Post result = postService.initializePostInfo(postId);

        // then
        assertThat(result.getText()).isEqualTo(updateText);
        assertThat(result.getTitle()).isEqualTo(updateTitle);
        assertThat(result.getPhotos()).hasSize(3);
    }
}
