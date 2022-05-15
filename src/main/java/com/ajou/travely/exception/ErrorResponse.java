package com.ajou.travely.exception;

public class ErrorResponse {
    private final int status;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
