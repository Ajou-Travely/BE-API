package com.ajou.travely.domain.error;

public enum ErrorCode {
    USER_NOT_FOUND(400, "USER RECORD IS NOT FOUND"),
    COST_NOT_FOUND(400, "COST RECORD IS NOT FOUND"),
    PHOTO_NOT_FOUND(400, "PHOTO RECORD IS NOT FOUND"),
    PLACE_NOT_FOUND(400, "PLACE RECORD IS NOT FOUND"),
    POST_NOT_FOUND(400, "POST RECORD IS NOT FOUND"),
    SCHEDULE_NOT_FOUND(400, "SCHEDULE RECORD IS NOT FOUND"),
    TRAVEL_NOT_FOUND(400, "TRAVEL RECORD IS NOT FOUND"),
    ;

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
