package com.dietiestates.api.errorhandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.api.dto.ExceptionResponse;
import com.dietiestates.api.exception.notfound.RealEstateNotFoundException;

@RestControllerAdvice
public class RealEstateExceptionHandler {

	@ExceptionHandler(RealEstateNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(RealEstateNotFoundException exception) {
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
