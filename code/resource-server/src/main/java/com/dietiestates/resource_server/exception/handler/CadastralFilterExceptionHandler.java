package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.notfound.CadastralFilterNotFoundException;
import com.dietiestates.resource_server.exception.notowned.CadastralFilterNotOwnedBySearchException;

@RestControllerAdvice
public class CadastralFilterExceptionHandler {

    @ExceptionHandler(CadastralFilterNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(CadastralFilterNotFoundException exception){
        return AppException.responseEntityFactory(exception);
    }

    @ExceptionHandler(CadastralFilterNotOwnedBySearchException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(CadastralFilterNotOwnedBySearchException exception){
        return AppException.responseEntityFactory(exception);
    }
}
