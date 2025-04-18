package com.inditex.infrastructure.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
