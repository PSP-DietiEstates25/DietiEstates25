package com.dietiestates.api.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.api.dto.response.ExceptionResponse;
import com.dietiestates.api.exception.notfound.DetailNotFoundException;

@RestControllerAdvice
public class DetailExceptionHandler {

	@ExceptionHandler(DetailNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(DetailNotFoundException exception) {
        return ResponseEntity.status(exception.getHttpErrorStatusCode()).body(
                ExceptionResponse.builder()
                .businessErrorCode(exception.getBusinessErrorCode())
                .businessErrorMessage(exception.getMessage())
                .build()
        			);
    }
}
