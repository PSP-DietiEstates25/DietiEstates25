package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.GeographicalPositionNotFoundException;
import com.dietiestates.resource_server.exception.notowned.GeographicalPositionNotOwnedByDetailException;

@RestControllerAdvice
public class GeographicalPositionExceptionHandler {

    @ExceptionHandler(GeographicalPositionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(GeographicalPositionNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(GeographicalPositionNotOwnedByDetailException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedBy(GeographicalPositionNotOwnedByDetailException exception){
        return AppException.responseEntityFactory(exception);
    }
}
