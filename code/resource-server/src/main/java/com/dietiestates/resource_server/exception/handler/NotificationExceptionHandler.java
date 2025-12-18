package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByUserException;

@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotificationNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(NotificationNotOwnedByUserException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedBy(NotificationNotOwnedByUserException exception) {
        return AppException.responseEntityFactory(exception);
    }
}

