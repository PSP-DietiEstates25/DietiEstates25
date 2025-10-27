package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.DetailNotFoundException;

@RestControllerAdvice
public class DetailExceptionHandler {

    @ExceptionHandler(DetailNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(DetailNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }
}
