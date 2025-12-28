package com.dietiestates.auth.exception.handler;

import com.dietiestates.auth.exception.AppException;
import com.dietiestates.auth.exception.notfound.DefaultAccountNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.auth.dto.response.ExceptionResponse;
import com.dietiestates.auth.exception.alreadyexists.DefaultAccountAlreadyExistsException;

@RestControllerAdvice
public class DefaultAccountExceptionHandler {

    @ExceptionHandler(DefaultAccountAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExists(DefaultAccountAlreadyExistsException exception){
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(DefaultAccountNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(DefaultAccountNotFoundException exception){
        return AppException.responseEntityFactory(exception);
    }
}
