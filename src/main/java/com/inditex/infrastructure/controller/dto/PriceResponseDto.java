package com.inditex.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que representa la respuesta del endpoint de precios.
 * Contiene todos los datos necesarios del precio aplicable.
 */
public record PriceResponseDto(
        Long productId,
        Long brandId,
        Long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency
) {
    @Override
    public String toString() {
        return "PriceResponseDto{" +
                "productId=" + productId +
                ", brandId=" + brandId +
                ", priceList=" + priceList +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
