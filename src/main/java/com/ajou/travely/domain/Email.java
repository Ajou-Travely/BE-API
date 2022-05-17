package com.ajou.travely.domain;

import lombok.Getter;

@Getter
public class Email {
    private final String email;

    public Email(String email) {
        this.email = email;
    }
}
