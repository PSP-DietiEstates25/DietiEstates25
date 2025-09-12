package com.dietiestates.api.errorhandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.api.dto.ExceptionResponse;
import com.dietiestates.api.exception.notfound.ServicesNotFoundException;

@RestControllerAdvice
public class ServicesExceptionHandler {

	@ExceptionHandler(ServicesNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(ServicesNotFoundException exception) {
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
