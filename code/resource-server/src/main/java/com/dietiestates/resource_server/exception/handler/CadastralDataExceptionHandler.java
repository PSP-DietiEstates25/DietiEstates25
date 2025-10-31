package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.CadastralDataNotFoundException;
import com.dietiestates.resource_server.exception.notowned.CadastralDataNotOwnedByRealEstateException;

@RestControllerAdvice
public class CadastralDataExceptionHandler {

    @ExceptionHandler(CadastralDataNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(CadastralDataNotFoundException exception){
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(CadastralDataNotOwnedByRealEstateException.class)
    public ResponseEntity<ExceptionResponse> handleNotOwnedBy(CadastralDataNotOwnedByRealEstateException exception){
        return AppException.responseEntityFactory(exception);
    }
}
