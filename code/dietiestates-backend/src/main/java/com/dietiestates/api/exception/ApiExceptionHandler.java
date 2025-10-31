package com.dietiestates.api.exception;

import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

  private static final Logger log = Logger.getLogger(ApiExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    log.warn("Validation error", ex);
    var first = ex.getBindingResult().getFieldErrors().stream().findFirst();
    String message = first.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
        .orElse("Validation error");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", "ValidationError", "message", message));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, Object>> handleAccess(AccessDeniedException ex) {
    log.warn("Access denied", ex);
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(Map.of("error", "AccessDenied", "message", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
    log.error("Unhandled exception", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of(
            "error", ex.getClass().getSimpleName(),
            "message", String.valueOf(ex.getMessage())));
  }

  @ExceptionHandler(BadCredentialsException.class)
  ResponseEntity<?> badCreds(BadCredentialsException e) {
    return ResponseEntity.badRequest().body(Map.of("message", "Current password not valid"));
  }

  @ExceptionHandler({ UsernameNotFoundException.class, EntityNotFoundException.class })
  ResponseEntity<?> notFound(RuntimeException e) {
    return ResponseEntity.status(404).body(Map.of("message", "User not found"));
  }

}
