package com.inditex.domain.exception;

import java.time.LocalDateTime;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(Long productId, Long brandId, LocalDateTime date) {
        super("No se encontró precio para producto " + productId +
                ", marca " + brandId +
                " en fecha " + date.toString());
    }
}

