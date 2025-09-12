package com.dietiestates.api.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum BusinessErrorCodes {
	
	//AUTH ERRORS
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, HttpStatus.BAD_REQUEST, "Login and / or Password is incorrect"),
    
    //NOT FOUND ERRORS
    NOT_FOUND(5555, HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1004, HttpStatus.NOT_FOUND, "User not found"),
    NOTIFICATION_NOT_FOUND(2004, HttpStatus.NOT_FOUND, "Notification not found"),
    NOTIFICATION_CATEGORY_NOT_FOUND(3004, HttpStatus.NOT_FOUND, "Notification category not found"),
    ADMIN_NOT_FOUND(4004, HttpStatus.NOT_FOUND, "Admin not found"),
    ESTATE_AGENT_NOT_FOUND(5004, HttpStatus.NOT_FOUND, "Estate agent not found"),
    OFFER_NOT_FOUND(6004, HttpStatus.NOT_FOUND, "Offer not found"),
    VISIT_NOT_FOUND(7004, HttpStatus.NOT_FOUND, "Visit not found"),
    PROPOSAL_NOT_FOUND(8004, HttpStatus.NOT_FOUND, "Proposal not found"),
    REAL_ESTATE_NOT_FOUND(9004, HttpStatus.NOT_FOUND, "Real estate not found"),
    DETAILS_NOT_FOUND(10004, HttpStatus.NOT_FOUND, "Details not found"),
    GEOGRAPHICAL_POSITION_NOT_FOUND(11004, HttpStatus.NOT_FOUND, "Geographical position not found"),
    DATA_NOT_FOUND(12004, HttpStatus.NOT_FOUND, "Data not found"),
    SERVICES_NOT_FOUND(13004, HttpStatus.NOT_FOUND, "Services not found"),
    SEARCH_NOT_FOUND(14004, HttpStatus.NOT_FOUND, "Search not found")
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
    
    BusinessErrorCodes(int code, HttpStatus status) {
        this.code = code;
        this.httpStatus = status;
        this.message = "";
    }
}