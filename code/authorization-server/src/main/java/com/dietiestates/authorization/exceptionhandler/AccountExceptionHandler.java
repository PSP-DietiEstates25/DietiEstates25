package com.dietiestates.authorization.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.authorization.dto.response.ExceptionResponse;
import com.dietiestates.authorization.exception.alreadyexists.AccountAlreadyExistsException;

@RestControllerAdvice
public class AccountExceptionHandler {

	@ExceptionHandler(AccountAlreadyExistsException.class)
	public ResponseEntity<ExceptionResponse> handleAlreadyExists(AccountAlreadyExistsException exception){
		return ResponseEntity.status(exception.getHttpErrorStatusCode()).body(
				ExceptionResponse.builder()
				.businessErrorCode(exception.getBusinessErrorCode())
				.businessErrorMessage(exception.getMessage())
				.build()
				);
	}
}
