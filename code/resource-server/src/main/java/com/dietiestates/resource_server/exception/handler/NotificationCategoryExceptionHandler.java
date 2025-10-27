package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.NotificationCategoryNotFoundException;

@RestControllerAdvice
public class NotificationCategoryExceptionHandler {

    @ExceptionHandler(NotificationCategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotificationCategoryNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }
}
