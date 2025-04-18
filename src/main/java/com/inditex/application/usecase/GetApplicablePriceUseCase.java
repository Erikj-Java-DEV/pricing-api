package com.inditex.application.usecase;

import com.inditex.domain.model.Price;
import com.inditex.domain.service.PriceService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GetApplicablePriceUseCase {

    private final PriceService priceService;

    public GetApplicablePriceUseCase(PriceService priceService) {
        this.priceService = priceService;
    }

    public Price execute(Long productId, Long brandId, LocalDateTime date) {
        return priceService.getApplicablePrice(productId, brandId, date);
    }
}
