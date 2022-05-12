package com.ajou.travely.controller.post.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class PostCreateRequestDto {

    @NotBlank(message = "schedule Id가 필요합니다.")
    private Long scheduleId;

    @NotBlank(message = "제목이 필요합니다.")
    private String title;

    private String text;

    private List<MultipartFile> files;

}
