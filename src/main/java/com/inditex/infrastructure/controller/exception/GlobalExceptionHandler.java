package com.inditex.infrastructure.controller.exception;

import com.inditex.domain.exception.PriceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja casos de negocio: no se encontró un precio
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePriceNotFound(PriceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", ex.getMessage(),
                "code", HttpStatus.NOT_FOUND.value()
        ));
    }

    // Maneja errores de parámetros faltantes o mal formateados
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", "Solicitud malformada: " + ex.getMessage(),
                "code", HttpStatus.BAD_REQUEST.value()
        ));
    }

    // Fallback genérico para RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", "Error inesperado: " + ex.getMessage(),
                "code", HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }
}
