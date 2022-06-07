package com.ajou.travely.controller.admin;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.notice.dto.NoticeCreateRequestDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.controller.notice.dto.SimpleNoticeResponseDto;
import com.ajou.travely.service.NoticeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v1/admin/notices")
@RestController
public class NoticeAdminController {

    private final NoticeService noticeService;

    @GetMapping()
    ResponseEntity<Page<SimpleNoticeResponseDto>> showAllNotices(@PageableDefault(
            sort = {"id"},
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return ResponseEntity.ok(noticeService.getNotices(pageable));
    }

    @GetMapping("/{noticeId}")
    ResponseEntity<NoticeResponseDto> showNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<NoticeResponseDto> createNotice(@LoginUser SessionUser sessionUser,
        @Valid @ModelAttribute NoticeCreateRequestDto requestDto) {
        return ResponseEntity.ok(noticeService.createNotice(sessionUser.getUserId(), requestDto));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
