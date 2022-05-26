package com.ajou.travely.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int status;
    private final String message;
    private final String exceptionMessage;

    public ErrorResponse(ErrorCode errorCode, String exceptionMessage) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.exceptionMessage = exceptionMessage;
    }
}
