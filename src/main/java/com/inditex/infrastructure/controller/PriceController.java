package com.inditex.infrastructure.controller;

import com.inditex.application.usecase.GetApplicablePriceUseCase;
import com.inditex.domain.model.Price;
import com.inditex.infrastructure.controller.dto.PriceResponseDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final GetApplicablePriceUseCase useCase;

    public PriceController(GetApplicablePriceUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Controlador REST para consultar el precio aplicable a un producto en un momento dado.
     *
     * Endpoint: GET /api/prices
     * Parámetros:
     *  - date: fecha y hora de aplicación del precio (ISO-8601)
     *  - productId: identificador del producto
     *  - brandId: identificador de la cadena/marca
     *
     * Devuelve el precio que aplica basado en prioridad, fecha y coincidencia de producto/marca.
     */
    @GetMapping
    public ResponseEntity<PriceResponseDto> getPrice(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam("productId") Long productId,
            @RequestParam("brandId") Long brandId
    ) {
        Price price = useCase.execute(productId, brandId, date);

        PriceResponseDto response = new PriceResponseDto(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice(),
                price.getCurr()
        );

        return ResponseEntity.ok(response);
    }
}
