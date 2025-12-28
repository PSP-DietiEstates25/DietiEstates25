package com.dietiestates.auth.exception.handler;

import com.dietiestates.auth.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.auth.dto.response.ExceptionResponse;
import com.dietiestates.auth.exception.notfound.RoleNotFoundException;

@RestControllerAdvice
public class RoleExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(RoleNotFoundException exception){
        return AppException.responseEntityFactory(exception);
    }
}
