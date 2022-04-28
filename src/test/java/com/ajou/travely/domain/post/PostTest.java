package com.ajou.travely.domain.post;

import com.ajou.travely.domain.Place;
import com.ajou.travely.domain.Post;
import com.ajou.travely.domain.Schedule;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.domain.user.Type;
import com.ajou.travely.domain.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    Travel travel = Travel.builder()
        .title("place title")
        .managerId(1L)
        .memo("")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now())
        .build();
    Place place = Place.builder()
        .x(123.123)
        .y(345.1354)
        .placeName("place name")
        .addressName("주소1")
        .addressRoadName("주소2")
        .placeUrl("urlurl")
        .build();
    Schedule schedule = Schedule.builder()
        .travel(travel)
        .place(place)
        .startTime(LocalDateTime.now())
        .endTime(LocalDateTime.now())
        .build();
    User user = User.builder()
        .type(Type.USER)
        .email("test@email.com")
        .name("user name")
        .phoneNumber("010-1111-1111")
        .kakaoId(12345152L)
        .build();

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
