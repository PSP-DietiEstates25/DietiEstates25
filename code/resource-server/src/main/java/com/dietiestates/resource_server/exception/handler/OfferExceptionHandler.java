package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.OfferNotFoundException;
import com.dietiestates.resource_server.exception.notowned.OfferNotOwnedByRealEstateException;

@RestControllerAdvice
public class OfferExceptionHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(OfferNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(OfferNotOwnedByRealEstateException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedBy(OfferNotOwnedByRealEstateException exception) {
        return AppException.responseEntityFactory(exception);
    }
}
