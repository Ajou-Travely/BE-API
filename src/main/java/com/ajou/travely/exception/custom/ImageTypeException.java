package com.ajou.travely.exception.custom;

import com.ajou.travely.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ImageTypeException extends RuntimeException {
    private final ErrorCode errorCode;

    public ImageTypeException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
