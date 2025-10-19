package com.dietiestates.resourceserver.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resourceserver.dto.response.ExceptionResponse;
import com.dietiestates.resourceserver.exception.notfound.NotificationNotFoundException;
import com.dietiestates.resourceserver.exception.notowned.NotificationNotOwnedByNotificationCategoryException;

@RestControllerAdvice
public class NotificationExceptionHandler {

	@ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(NotificationNotFoundException exception) {
        return ResponseEntity
                .status(exception.getHttpErrorStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exception.getBusinessErrorCode())
                                .businessErrorMessage(exception.getMessage())
                                .build()
                );
    }
	
	@ExceptionHandler(NotificationNotOwnedByNotificationCategoryException.class)
	public ResponseEntity<ExceptionResponse> handleNotOwnedBy(NotificationNotOwnedByNotificationCategoryException exception) {
        return ResponseEntity
                .status(exception.getHttpErrorStatusCode())
                .body(
                        ExceptionResponse.builder()
                                .businessErrorCode(exception.getBusinessErrorCode())
                                .businessErrorMessage(exception.getMessage())
                                .build()
                );
    }
}
