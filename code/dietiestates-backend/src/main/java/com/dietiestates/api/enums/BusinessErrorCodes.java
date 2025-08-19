package com.dietiestates.api.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum BusinessErrorCodes {
	
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, HttpStatus.BAD_REQUEST, "Login and / or Password is incorrect"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}