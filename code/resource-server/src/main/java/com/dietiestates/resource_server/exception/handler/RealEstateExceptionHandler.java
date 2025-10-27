package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.RealEstateNotFoundException;

@RestControllerAdvice
public class RealEstateExceptionHandler {

    @ExceptionHandler(RealEstateNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(RealEstateNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }
}
