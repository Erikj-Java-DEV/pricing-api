package com.inditex.infrastructure.repository;

import com.inditex.domain.model.Price;
import com.inditex.domain.repository.PriceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class JpaPriceRepository implements PriceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime date) {

        /*
        Filtra por brandId, productId

        Filtra por rango de fechas (startDate <= ? AND endDate >= ?)

        Ordena por priority descendente

        Devuelve solo 1 resultado (el más prioritario)
         */

        String sql = """
                SELECT * FROM PRICES 
                WHERE PRODUCT_ID = :productId 
                  AND BRAND_ID = :brandId 
                  AND START_DATE <= :applicationDate 
                  AND END_DATE >= :applicationDate 
                ORDER BY PRIORITY DESC 
                LIMIT 1
                """;

        Query query = entityManager.createNativeQuery(sql, Price.class);
        query.setParameter("productId", productId);
        query.setParameter("brandId", brandId);
        query.setParameter("applicationDate", date);

        return query.getResultList().stream().findFirst();
    }
}

