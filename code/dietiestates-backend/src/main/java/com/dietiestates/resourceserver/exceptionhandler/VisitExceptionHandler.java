package com.dietiestates.resourceserver.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resourceserver.dto.response.ExceptionResponse;
import com.dietiestates.resourceserver.exception.notfound.VisitNotFoundException;
import com.dietiestates.resourceserver.exception.notowned.VisitNotOwnedByRealEstateException;

@RestControllerAdvice
public class VisitExceptionHandler {

	@ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(VisitNotFoundException exception) {
        return ResponseEntity
                .status(exception.getHttpErrorStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exception.getBusinessErrorCode())
                                .businessErrorMessage(exception.getMessage())
                                .build()
                );
    }
	
	@ExceptionHandler(VisitNotOwnedByRealEstateException.class)
	public ResponseEntity<ExceptionResponse> handleNotOwnedBy(VisitNotOwnedByRealEstateException exception) {
        return ResponseEntity
                .status(exception.getHttpErrorStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exception.getBusinessErrorCode())
                                .businessErrorMessage(exception.getMessage())
                                .build()
                );
    }
}
