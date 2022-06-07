package com.ajou.travely.controller.notice;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.notice.dto.NoticeCreateRequestDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.controller.notice.dto.SimpleNoticeResponseDto;
import com.ajou.travely.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequestMapping("v1/notices")
@RequiredArgsConstructor
@RestController
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    // notice pagination
    @GetMapping("")
    public ResponseEntity<Page<SimpleNoticeResponseDto>> showNotices(
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<SimpleNoticeResponseDto> notices = noticeService.getNotices(pageable);
        return ResponseEntity.ok(notices);
    }


}
