package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.AppException;
import com.dietiestates.resource_server.exception.notfound.AdminNotFoundException;
import com.dietiestates.resource_server.exception.notfound.SearchNotFoundException;
import com.dietiestates.resource_server.exception.notowned.SearchNotOwnedByUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SearchExceptionHandler {

    @ExceptionHandler(SearchNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(SearchNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(SearchNotOwnedByUserException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(SearchNotOwnedByUserException exception) {
        return AppException.responseEntityFactory(exception);
    }
}
