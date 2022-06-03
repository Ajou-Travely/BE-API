package com.ajou.travely.service;

import com.ajou.travely.controller.notice.dto.NoticeCreateRequestDto;
import com.ajou.travely.controller.notice.dto.NoticeResponseDto;
import com.ajou.travely.domain.Notice;
import com.ajou.travely.domain.Photo;
import com.ajou.travely.domain.user.User;
import com.ajou.travely.exception.ErrorCode;
import com.ajou.travely.exception.custom.RecordNotFoundException;
import com.ajou.travely.repository.NoticeRepository;
import com.ajou.travely.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {
    private final PhotoService photoService;

    private final NoticeRepository noticeRepository;

    private final UserRepository userRepository;

    public NoticeResponseDto createNotice(Long authorId, NoticeCreateRequestDto requestDto) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RecordNotFoundException(
                        "해당 ID를 가진 사용자를 찾을 수 없습니다.",
                        ErrorCode.USER_NOT_FOUND
                ));
        Notice notice = noticeRepository.save(Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(author)
                .createdAt(LocalDateTime.now())
                .build());
        List<Photo> photos = new ArrayList<>();
        if (!requestDto.getPhotos().isEmpty()) {
            photos = photoService.createPhotosOfNotice(notice, requestDto.getPhotos());
        }
        return new NoticeResponseDto(notice);
    }
}
