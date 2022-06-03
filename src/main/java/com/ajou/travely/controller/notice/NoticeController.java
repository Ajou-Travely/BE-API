package com.ajou.travely.controller.notice;

import com.ajou.travely.config.auth.LoginUser;
import com.ajou.travely.config.auth.SessionUser;
import com.ajou.travely.controller.notice.dto.NoticeCreateRequestDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("v1/notices")
@RequiredArgsConstructor
@RestController
public class NoticeController {
    private final NoticeService noticeService;

    @PostMapping(value = "", consumes = "multipart/form-data")
    public ResponseEntity<NoticeResponseDto> createNotice(@LoginUser SessionUser sessionUser,
                                                          @Valid @RequestPart NoticeCreateRequestDto requestDto) {
        return ResponseEntity.ok(this.noticeService.createNotice(sessionUser.getUserId(), requestDto));
    }
}
