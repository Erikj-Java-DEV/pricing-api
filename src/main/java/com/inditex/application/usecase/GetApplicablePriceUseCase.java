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

    /**
     * Caso de uso para obtener el precio aplicable según la fecha de aplicación,
     * el identificador del producto y el identificador de la marca.
     *
     * Aplica la lógica de negocio correspondiente para seleccionar el precio correcto.
     */
    public Price execute(Long productId, Long brandId, LocalDateTime date) {
        return priceService.getApplicablePrice(productId, brandId, date);
    }
}
