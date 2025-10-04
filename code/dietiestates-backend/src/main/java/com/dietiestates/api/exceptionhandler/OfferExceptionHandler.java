package com.dietiestates.api.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.api.dto.response.ExceptionResponse;
import com.dietiestates.api.exception.notfound.OfferNotFoundException;
import com.dietiestates.api.exception.notowned.OfferNotOwnedByRealEstateException;

@RestControllerAdvice
public class OfferExceptionHandler {

	@ExceptionHandler(OfferNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(OfferNotFoundException exception) {
        return ResponseEntity.status(exception.getHttpErrorStatusCode()).body(
        		ExceptionResponse.builder()
        			.businessErrorCode(exception.getBusinessErrorCode())
        			.businessErrorMessage(exception.getMessage())
                .build()
        			);
    }
	
	@ExceptionHandler(OfferNotOwnedByRealEstateException.class)
	public ResponseEntity<ExceptionResponse> handleNotOwnedBy(OfferNotOwnedByRealEstateException exception) {
        return ResponseEntity.status(exception.getHttpErrorStatusCode()).body(
        		ExceptionResponse.builder()
        			.businessErrorCode(exception.getBusinessErrorCode())
        			.businessErrorMessage(exception.getMessage())
                .build()
        			);
    }
}
