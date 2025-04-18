package com.inditex.domain.exception;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(Long productId, Long brandId, String date) {
        super("No se encontró precio para producto " + productId +
                ", marca " + brandId +
                " en fecha " + date);
    }
}
