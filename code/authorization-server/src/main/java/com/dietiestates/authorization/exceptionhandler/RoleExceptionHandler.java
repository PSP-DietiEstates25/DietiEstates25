package com.dietiestates.authorization.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.authorization.dto.response.ExceptionResponse;
import com.dietiestates.authorization.exception.notfound.RoleNotFoundException;

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
