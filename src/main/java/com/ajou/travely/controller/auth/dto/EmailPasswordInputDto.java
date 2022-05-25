package com.ajou.travely.controller.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class EmailPasswordInputDto {
    @NotNull(message = "이메일을 입력해주세요.")
    private String email;
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    public EmailPasswordInputDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
