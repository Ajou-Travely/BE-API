package com.ajou.travely.repository;

import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.travel.Travel;
import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.domain.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
        "spring.mail.password=temptemptemptemp"
})
@Transactional
public class PostRepositoryTest {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PlaceRepository placeRepository;

    public Travel travel;
    public Place place;
    public User user;
    public Schedule schedule;

//    @BeforeEach
//    public void setUp() {
//        travel = travelRepository.save(Travel.builder()
//                .title("place title")
//                .managerId(1L)
//                .memo("")
//                .build());
//        place = placeRepository.save(Place.builder()
//                .lat(123.123)
//                .lng(345.1354)
//                .placeName("place name")
//                .addressName("주소1")
//                .addressRoadName("주소2")
//                .placeUrl("urlurl")
//                .kakaoMapId(1L)
//                .build());
//        user = userRepository.save(User.builder()
//                .userType(UserType.USER)
//                .email("test@email.com")
//                .name("user name")
//                .phoneNumber("010-1111-1111")
//                .kakaoId(12345152L)
//                .build());
//        schedule = scheduleRepository.save(Schedule.builder()
//                .travel(travel)
//                .place(place)
//                .build());
//    }

    @Rollback
    @Test
    void 포스트_생성_성공() {
        // given
        Post post = Post.builder()
                .schedule(schedule)
                .user(user)
                .text("post content")
                .title("post title")
                .build();

        // when
        Post result = postRepository.save(post);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.toString()).isEqualTo(post.toString());
    }

    @Rollback
    @Test
    void 포스트_내용_수정() {
        // given
        Post post = Post.builder()
                .schedule(schedule)
                .user(user)
                .text("post content")
                .title("post title")
                .build();
        String updateTitle = "post title2";
        String updateText = "post content2";

        // when
        post.update(updateTitle, updateText);

        // then
        assertThat(post.getTitle()).isEqualTo(updateTitle);
        assertThat(post.getText()).isEqualTo(updateText);

    }

}
