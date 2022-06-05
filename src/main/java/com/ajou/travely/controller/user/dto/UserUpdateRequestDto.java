package com.ajou.travely.controller.user.dto;

import com.ajou.travely.domain.user.Mbti;
import com.ajou.travely.domain.user.Sex;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserUpdateRequestDto {
    @NotNull(message = "이름이 필요합니다.")
    private final String name;

    @NotNull(message = "전화번호가 필요합니다.")
    private final String phoneNumber;

    private final Mbti mbti;

    private final Sex sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;

    @Builder
    public UserUpdateRequestDto(@NonNull String name,
                                @NonNull String phoneNumber,
                                Mbti mbti,
                                Sex sex,
                                LocalDate birthday
        ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mbti = mbti;
        this.sex = sex;
        this.birthday = birthday;
    }

}
