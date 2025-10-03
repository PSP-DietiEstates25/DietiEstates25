package com.dietiestates.api.errorhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.api.dto.response.ExceptionResponse;
import com.dietiestates.api.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.api.exception.notowned.CadastralDataNotOwnedByRealEstateException;

@RestControllerAdvice
public class CadastralDataExceptionHandler {
	
	@ExceptionHandler(CadastralDataNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleNotFound(CadastralDataNotFoundException exception){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				ExceptionResponse.builder()
					.businessErrorCode(exception.getBusinessErrorCode())
					.businessErrorMessage(exception.getMessage())
					.build()
				);
	}

	@ExceptionHandler(CadastralDataNotOwnedByRealEstateException.class)
	public ResponseEntity<ExceptionResponse> handleNotOwnedBy(CadastralDataNotOwnedByRealEstateException exception){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
				ExceptionResponse.builder()
					.businessErrorCode(exception.getBusinessErrorCode())
					.businessErrorMessage(exception.getMessage())
					.build()
				);
	}
}
