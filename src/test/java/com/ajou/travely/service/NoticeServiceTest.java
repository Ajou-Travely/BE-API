package com.ajou.travely.service;

import com.ajou.travely.controller.notice.dto.NoticeCreateRequestDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.domain.Notice;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.domain.user.UserType;
import com.ajou.travely.repository.NoticeRepository;
import com.ajou.travely.repository.PhotoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(properties = {
        "auth.kakaoOauth2ClinetId=test",
        "auth.frontendRedirectUrl=test",
        "spring.mail.password=temptemptemptemp"
})
@Transactional
public class NoticeServiceTest {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private UserService userService;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("공지사항을 생성할 수 있다.")
    void testCreateNotice() {
        User author = userService.insertUser(
                User.builder()
                        .userType(UserType.USER)
                        .email("sophoca@ajou.ac.kr")
                        .name("홍성빈")
                        .phoneNumber("112")
                        .kakaoId(0L)
                        .build()
        );
        NoticeCreateRequestDto requestDto = NoticeCreateRequestDto.builder()
                .title("test title")
                .content("test content")
                .build();
        NoticeResponseDto notice = noticeService.createNotice(author.getId(), requestDto);
        Assertions.assertThat(notice.getAuthorInfo().getUserId()).isEqualTo(author.getId());
        Assertions.assertThat(notice.getContent()).isEqualTo("test content");
        Assertions.assertThat(notice.getPhotoInfos().size()).isEqualTo(0);

        em.clear();

        Optional<Notice> foundNotice = noticeRepository.findById(notice.getNoticeId());

        Assertions.assertThat(foundNotice.isEmpty()).isEqualTo(false);
        Assertions.assertThat(foundNotice.get().getId()).isEqualTo(notice.getNoticeId());
    }
}
