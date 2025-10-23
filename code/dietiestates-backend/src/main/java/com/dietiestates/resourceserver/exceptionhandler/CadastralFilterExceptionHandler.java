package com.dietiestates.resourceserver.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resourceserver.dto.response.ExceptionResponse;
import com.dietiestates.resourceserver.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.resourceserver.exception.notowned.CadastralFilterNotOwnedBySearchException;

@RestControllerAdvice
public class CadastralFilterExceptionHandler {

	@ExceptionHandler(CadastralFilterNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleNotFound(CadastralFilterNotFoundException exception){
		return ResponseEntity.status(exception.getHttpErrorStatusCode()).body(
				ExceptionResponse.builder()
				.businessErrorCode(exception.getBusinessErrorCode())
				.businessErrorMessage(exception.getMessage())
				.build()
				);
	}
	
	@ExceptionHandler(CadastralFilterNotOwnedBySearchException.class)
	public ResponseEntity<ExceptionResponse> handleNotFound(CadastralFilterNotOwnedBySearchException exception){
		return ResponseEntity.status(exception.getHttpErrorStatusCode()).body(
				ExceptionResponse.builder()
				.businessErrorCode(exception.getBusinessErrorCode())
				.businessErrorMessage(exception.getMessage())
				.build()
				);
	}
}
