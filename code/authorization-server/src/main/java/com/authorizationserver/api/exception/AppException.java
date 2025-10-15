package com.authorizationserver.api.exception;

import org.springframework.http.HttpStatus;

import com.authorizationserver.api.enums.BusinessErrorCodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {

	private static final long serialVersionUID = 484890383291704422L;
	private final int businessErrorCode;
	private final HttpStatus httpErrorStatusCode;
	
	public AppException(BusinessErrorCodes error) {
		super(error.getMessage());
		this.businessErrorCode = error.getCode();
		this.httpErrorStatusCode = error.getHttpStatus();
	}
}
