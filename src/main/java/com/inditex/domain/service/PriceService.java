package com.inditex.domain.service;

import com.inditex.domain.model.Price;

import java.time.LocalDateTime;

public interface PriceService {
    Price getApplicablePrice(Long productId, Long brandId, LocalDateTime date);
}
