package com.ajou.travely.exception.custom;

import com.ajou.travely.exception.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicatedPrimaryKeyException extends RuntimeException {
    private final ErrorCode errorCode;

    public DuplicatedPrimaryKeyException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
