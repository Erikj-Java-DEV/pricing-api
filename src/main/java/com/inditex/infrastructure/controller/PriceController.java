package com.inditex.infrastructure.controller;

import com.inditex.application.usecase.GetApplicablePriceUseCase;
import com.inditex.domain.model.Price;
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

    @GetMapping
    public ResponseEntity<Price> getPrice(
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date,

            @RequestParam("productId")
            Long productId,

            @RequestParam("brandId")
            Long brandId
    ) {
        Price price = useCase.execute(productId, brandId, date);
        return ResponseEntity.ok(price);
    }
}
