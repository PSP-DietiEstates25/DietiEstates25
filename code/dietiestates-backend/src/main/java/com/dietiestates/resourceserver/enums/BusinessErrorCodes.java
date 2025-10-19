package com.dietiestates.resourceserver.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum BusinessErrorCodes {
	
	//AUTH ERRORS
    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    
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
    DETAIL_NOT_FOUND(10004, HttpStatus.NOT_FOUND, "Detail not found"),
    GEOGRAPHICAL_POSITION_NOT_FOUND(11004, HttpStatus.NOT_FOUND, "Geographical position not found"),
    UTILITY_NOT_FOUND(12004, HttpStatus.NOT_FOUND, "Utility not found"),
    SEARCH_NOT_FOUND(13004, HttpStatus.NOT_FOUND, "Search not found"),
    CADASTRAL_DATA_NOT_FOUND(14004, HttpStatus.NOT_FOUND, "CadastralData not found"),
    CADASTRAL_FILTER_NOT_FOUND(15004, HttpStatus.NOT_FOUND, "CadastralFilter not found"),
    ADMIN_ROLE_NOT_FOUND(16004, HttpStatus.NOT_FOUND, "Admin role not found"),
    ESTATE_AGENT_ROLE_NOT_FOUND(16004, HttpStatus.NOT_FOUND, "Estate agent role not found"),
    USER_ROLE_NOT_FOUND(16004, HttpStatus.NOT_FOUND, "User role not found"),
    ROLE_NOT_FOUND(16004, HttpStatus.NOT_FOUND, "Role not found"),
    ACCOUNT_NOT_FOUND(17004, HttpStatus.NOT_FOUND, "Account not found"),
  
    //FORBIDDEN ERRORS
    CADASTRAL_DATA_NOT_OWNED_BY_REAL_ESTATE(14003, HttpStatus.FORBIDDEN, "Cadastral data not owned by real estate"),
    CADASTRAL_FILTER_NOT_OWNED_BY_SEARCH(15003, HttpStatus.FORBIDDEN, "Cadastral filter not owned by search"),
    UTILITY_NOT_OWNED_BY_DETAIL(12003, HttpStatus.FORBIDDEN, "Utility not owned by detail"),
    GEOGRAPHICAL_POSITION_NOT_OWNED_BY_DETAIL(11003, HttpStatus.FORBIDDEN, "Geographical position not owned by detail"),
    OFFER_NOT_OWNED_BY_REAL_ESTATE(6003, HttpStatus.FORBIDDEN, "Offer not owned by real estate"),
    VISIT_NOT_OWNED_BY_REAL_ESTATE(7003, HttpStatus.FORBIDDEN, "Visit not owned by real estate"),
    NOTIFICATION_NOT_OWNED_BY_NOTIFICATION_CATEGORY(2003, HttpStatus.FORBIDDEN, "Notification not owned by notification category"),
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