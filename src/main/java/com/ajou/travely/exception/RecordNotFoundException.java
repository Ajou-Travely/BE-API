package com.ajou.travely.exception;

import com.ajou.travely.domain.error.ErrorCode;
import lombok.Getter;

@Getter
public class RecordNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public RecordNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
