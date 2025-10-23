package com.dietiestates.resourceserver.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resourceserver.dto.response.ExceptionResponse;
import com.dietiestates.resourceserver.exception.notfound.UtilityNotFoundException;
import com.dietiestates.resourceserver.exception.notowned.UtilityNotOwnedByDetailException;

@RestControllerAdvice
public class UtilityExceptionHandler {

	@ExceptionHandler(UtilityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleNotFound(UtilityNotFoundException exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					ExceptionResponse.builder()
					.businessErrorCode(exception.getBusinessErrorCode())
					.businessErrorMessage(exception.getMessage())
					.build()
				);
	}
	
	@ExceptionHandler(UtilityNotOwnedByDetailException.class)
	public ResponseEntity<ExceptionResponse> handleNotOwnedBy(UtilityNotOwnedByDetailException exception){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				ExceptionResponse.builder()
				.businessErrorCode(exception.getBusinessErrorCode())
				.businessErrorMessage(exception.getMessage())
				.build()
			);
	}
}
