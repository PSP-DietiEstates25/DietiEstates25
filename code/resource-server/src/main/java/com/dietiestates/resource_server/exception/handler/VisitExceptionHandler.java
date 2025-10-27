package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.VisitNotFoundException;
import com.dietiestates.resource_server.exception.notowned.VisitNotOwnedByRealEstateException;

@RestControllerAdvice
public class VisitExceptionHandler {

    @ExceptionHandler(VisitNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(VisitNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(VisitNotOwnedByRealEstateException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedBy(VisitNotOwnedByRealEstateException exception) {
        return AppException.responseEntityFactory(exception);
    }
}
