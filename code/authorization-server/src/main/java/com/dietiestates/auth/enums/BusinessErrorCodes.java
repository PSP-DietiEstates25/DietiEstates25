package com.dietiestates.auth.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum BusinessErrorCodes {

    NO_CODE(1, HttpStatus.NOT_IMPLEMENTED, "No code"),
    ACCOUNT_LOCKED(1100, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(1200, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(1401, HttpStatus.UNAUTHORIZED, "Email and / or password is incorrect"),

    DEFAULT_ACCOUNT_NOT_FOUND(1404, HttpStatus.NOT_FOUND, "Account not found"),
    DEFAULT_ACCOUNT_BAD_REQUEST(1400, HttpStatus.BAD_REQUEST, "Default account bad request"),
    ROLE_NOT_FOUND(1504, HttpStatus.NOT_FOUND, "Role not found"),

    DEFAULT_ACCOUNT_ALREADY_EXISTS(1403, HttpStatus.FORBIDDEN, "Account already exists"),
    ;

    @Getter
    private final int code;

    @Getter
    private final HttpStatus httpStatus;

    @Getter
    private final String message;

    BusinessErrorCodes(int code, HttpStatus status, String message) {
        this.code = code;
        this.message = message;
        this.httpStatus = status;
    }
}
