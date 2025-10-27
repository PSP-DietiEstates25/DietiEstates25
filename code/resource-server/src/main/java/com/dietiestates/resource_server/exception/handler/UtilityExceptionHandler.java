package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.UtilityNotFoundException;
import com.dietiestates.resource_server.exception.notowned.UtilityNotOwnedByDetailException;

@RestControllerAdvice
public class UtilityExceptionHandler {

    @ExceptionHandler(UtilityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(UtilityNotFoundException exception){
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(UtilityNotOwnedByDetailException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedBy(UtilityNotOwnedByDetailException exception){
        return AppException.responseEntityFactory(exception);
    }
}
