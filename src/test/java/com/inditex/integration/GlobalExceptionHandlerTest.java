package com.inditex.integration;



import com.inditex.domain.exception.PriceNotFoundException;
import com.inditex.infrastructure.controller.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("✔️ PriceNotFoundException -> 404")
    void handlePriceNotFound() {
        // Crear una instancia válida de la excepción
        PriceNotFoundException ex = new PriceNotFoundException(35455L, 1L, LocalDateTime.of(2020, 6, 14, 10, 0));

        ResponseEntity<Map<String, Object>> response = handler.handlePriceNotFound(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody().get("message").toString())
                .contains("No se encontró precio para producto 35455");
    }


    @Test
    @DisplayName("✔️ MissingServletRequestParameterException -> 400")
    void handleMissingParams() {
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("productId", "Long");
        ResponseEntity<Map<String, Object>> response = handler.handleMissingParams(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().get("message").toString()).contains("productId");
    }

    @Test
    @DisplayName("✔️ MethodArgumentTypeMismatchException -> 400")
    void handleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "invalid-date", String.class, "date", null, new IllegalArgumentException("Error")
        );
        ResponseEntity<Map<String, Object>> response = handler.handleTypeMismatch(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().get("message").toString()).contains("date");
    }

    @Test
    @DisplayName("✔️ HttpMessageNotReadableException -> 400")
    void handleUnreadable() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("JSON inválido");
        ResponseEntity<Map<String, Object>> response = handler.handleUnreadable(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().get("message").toString()).contains("JSON inválido");
    }

    @DisplayName("✔️ MethodArgumentNotValidException -> 400")
    @Test
    void handleValidation() {
        // Mock de la excepción y sus dependencias
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = mock(FieldError.class);

        // Encadenamos las llamadas
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        when(fieldError.getDefaultMessage()).thenReturn("Campo requerido");

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody().get("message").toString()).contains("Campo requerido");
    }



    @Test
    @DisplayName("✔️ RuntimeException fallback -> 500")
    void handleRuntime() {
        RuntimeException ex = new RuntimeException("Algo inesperado ocurrió");
        ResponseEntity<Map<String, Object>> response = handler.handleRuntime(ex);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody().get("message").toString()).contains("Algo inesperado ocurrió");
    }
}

