package com.authenticationserver.api.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.authenticationserver.api.dto.response.ExceptionResponse;
import com.authenticationserver.api.exception.notfound.RoleNotFoundException;

@RestControllerAdvice
public class RoleExceptionHandler {

	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleNotFound(RoleNotFoundException exception){
		return ResponseEntity.status(exception.getHttpErrorStatusCode()).body(
				ExceptionResponse.builder()
				.businessErrorCode(exception.getBusinessErrorCode())
				.businessErrorMessage(exception.getMessage())
				.build()
				);
	}
}
