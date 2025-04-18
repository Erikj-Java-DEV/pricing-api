package com.inditex.application.service;

import com.inditex.domain.exception.PriceNotFoundException;
import com.inditex.domain.model.Price;
import com.inditex.domain.repository.PriceRepository;
import com.inditex.domain.service.PriceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementación del caso de uso GetApplicablePriceUseCase.
 *
 * Utiliza el repositorio de dominio para obtener el precio que cumpla:
 *  - Coincidencia de producto y marca
 *  - Fecha dentro del rango de validez
 *  - Mayor prioridad si hay más de uno
 */
@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price getApplicablePrice(Long productId, Long brandId, LocalDateTime date) {
        return priceRepository.findApplicablePrice(productId, brandId, date)
                .orElseThrow(() -> new PriceNotFoundException(productId, brandId, date.toString()));
    }
}
