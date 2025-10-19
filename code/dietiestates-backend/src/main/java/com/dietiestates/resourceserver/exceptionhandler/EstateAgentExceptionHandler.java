package com.dietiestates.resourceserver.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resourceserver.dto.response.ExceptionResponse;
import com.dietiestates.resourceserver.exception.notfound.EstateAgentNotFoundException;

@RestControllerAdvice
public class EstateAgentExceptionHandler {

	@ExceptionHandler(EstateAgentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(EstateAgentNotFoundException exception) {
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
