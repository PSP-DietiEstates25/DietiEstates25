package com.dietiestates.api.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.api.dto.response.ExceptionResponse;
import com.dietiestates.api.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.api.exception.notowned.GeographicalPositionNotOwnedByDetailException;

@RestControllerAdvice
public class GeographicalPositionExceptionHandler {

	@ExceptionHandler(GeographicalPositionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(GeographicalPositionNotFoundException exception) {
        return ResponseEntity
                .status(exception.getHttpErrorStatusCode())
                .body(
                		ExceptionResponse.builder()
                    .businessErrorCode(exception.getBusinessErrorCode())
                    .businessErrorMessage(exception.getMessage())
                    .build()
                );
    }
	
	@ExceptionHandler(GeographicalPositionNotOwnedByDetailException.class)
	public ResponseEntity<ExceptionResponse> handleNotOwnedBy(GeographicalPositionNotOwnedByDetailException exception){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				ExceptionResponse.builder()
				.businessErrorCode(exception.getBusinessErrorCode())
				.businessErrorMessage(exception.getMessage())
				.build()
				);
	}
}
