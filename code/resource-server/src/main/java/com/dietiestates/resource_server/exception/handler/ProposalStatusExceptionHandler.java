package com.dietiestates.resource_server.exception.handler;

import com.dietiestates.resource_server.dto.response.ExceptionResponse;
import com.dietiestates.resource_server.exception.AppException;
import com.dietiestates.resource_server.exception.notfound.ProposalStatusNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProposalStatusExceptionHandler {

    @ExceptionHandler(ProposalStatusNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(ProposalStatusNotFoundException exception) {
        return AppException.responseEntityFactory(exception);
    }
}
