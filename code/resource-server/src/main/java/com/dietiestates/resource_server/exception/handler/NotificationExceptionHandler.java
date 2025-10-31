package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resource_server.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

@RestControllerAdvice
public class NotificationExceptionHandler {

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotificationNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(NotificationNotOwnedByNotificationCategoryException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedBy(NotificationNotOwnedByNotificationCategoryException exception) {
        return AppException.responseEntityFactory(exception);
    }
}

