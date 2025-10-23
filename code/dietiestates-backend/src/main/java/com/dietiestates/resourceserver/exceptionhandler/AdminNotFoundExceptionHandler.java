package com.dietiestates.resourceserver.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resourceserver.dto.response.ExceptionResponse;
import com.dietiestates.resourceserver.exception.notfound.AdminNotFoundException;

@RestControllerAdvice
public class AdminNotFoundExceptionHandler {

	@ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(AdminNotFoundException exception) {
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
