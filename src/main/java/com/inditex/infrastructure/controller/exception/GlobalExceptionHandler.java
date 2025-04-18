package com.inditex.infrastructure.controller.exception;

import com.inditex.domain.exception.PriceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manejador global de excepciones para capturar errores comunes de la API.
 * Cada tipo de excepción es interceptada y traducida a una respuesta amigable.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Excepción de negocio: no se encontró un precio aplicable.
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePriceNotFound(PriceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", ex.getMessage(),
                "code", HttpStatus.NOT_FOUND.value()
        ));
    }

    /**
     * Parámetro faltante en la solicitud (por ejemplo, falta "productId").
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", "Parámetro faltante: " + ex.getParameterName(),
                "code", HttpStatus.BAD_REQUEST.value()
        ));
    }

    /**
     * Error al convertir un tipo de parámetro (por ejemplo, fecha inválida).
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", "Tipo de dato inválido para el parámetro: " + ex.getName(),
                "code", HttpStatus.BAD_REQUEST.value()
        ));
    }

    /**
     * JSON malformado o campos mal enviados en el cuerpo de la solicitud.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", "Cuerpo de la solicitud no legible: " + ex.getMessage(),
                "code", HttpStatus.BAD_REQUEST.value()
        ));
    }

    /**
     * Validaciones fallidas en parámetros anotados con @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", "Validación fallida: " + ex.getBindingResult().getFieldError().getDefaultMessage(),
                "code", HttpStatus.BAD_REQUEST.value()
        ));
    }

    /**
     * Fallback para cualquier excepción no controlada de tipo Runtime.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "message", "Error inesperado: " + ex.getMessage(),
                "code", HttpStatus.INTERNAL_SERVER_ERROR.value()
        ));
    }
}
