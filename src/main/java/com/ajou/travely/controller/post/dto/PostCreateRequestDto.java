package com.ajou.travely.controller.post.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class PostCreateRequestDto {

    @NotNull(message = "schedule Id가 필요합니다.")
    private Long scheduleId;

    @NotBlank(message = "제목이 필요합니다.")
    private String title;

    @NotBlank
    private String text;

    private List<MultipartFile> photos;

}
