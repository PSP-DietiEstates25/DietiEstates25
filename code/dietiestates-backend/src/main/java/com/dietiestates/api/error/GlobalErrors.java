package com.dietiestates.api.error;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalErrors {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,String>> handleIAE(IllegalArgumentException ex) {
    // per login fallito 401
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("error", ex.getMessage()));
  }

  // opzionale: per validazioni @Valid / @Validated
  @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,String>> handleValid(MethodArgumentNotValidException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", "Dati non validi"));
  }
}
