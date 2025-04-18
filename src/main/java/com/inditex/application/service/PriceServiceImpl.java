package com.inditex.application.service;

import com.inditex.domain.model.Price;
import com.inditex.domain.repository.PriceRepository;
import com.inditex.domain.service.PriceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price getApplicablePrice(Long productId, Long brandId, LocalDateTime date) {
        return priceRepository.findApplicablePrice(productId, brandId, date)
                .orElseThrow(() -> new RuntimeException("No price found for given criteria"));
    }
}
