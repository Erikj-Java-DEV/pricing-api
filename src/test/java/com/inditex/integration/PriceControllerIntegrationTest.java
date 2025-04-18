package com.inditex.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inditex.infrastructure.controller.dto.PriceResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    /**
     * Ejecuta una consulta contra el endpoint de precios y muestra en consola:
     * - Qué se buscó
     * - El precio esperado
     * - El resultado completo devuelto por el sistema
     */
    private void runTest(String label, String date, long productId, long brandId, BigDecimal expectedPrice, long expectedTariff) throws Exception {
        System.out.printf("🔍 %s | Buscando precio para producto=%d, marca=%d, fecha=%s%n", label, productId, brandId, date);

        MvcResult result = mockMvc.perform(get("/api/prices")
                        .param("date", date)
                        .param("productId", String.valueOf(productId))
                        .param("brandId", String.valueOf(brandId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(expectedPrice.doubleValue())))
                .andExpect(jsonPath("$.priceList", is((int) expectedTariff)))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        PriceResponseDto dto = objectMapper.readValue(json, PriceResponseDto.class);

        System.out.printf("✅ Precio aplicado: %.2f € | Tarifa: %d%n", dto.price(), dto.priceList());
        System.out.println("📦 DTO completo → " + dto);
        System.out.println();
    }

    @Test
    void test1() throws Exception {
        // Día 14 a las 10:00 → tarifa 1, precio 35.50
        runTest("Test 1", "2020-06-14T10:00:00", 35455L, 1L, new BigDecimal("35.50"), 1L);
    }

    @Test
    void test2() throws Exception {
        // Día 14 a las 16:00 → tarifa 2, precio 25.45 (mayor prioridad)
        runTest("Test 2", "2020-06-14T16:00:00", 35455L, 1L, new BigDecimal("25.45"), 2L);
    }

    @Test
    void test3() throws Exception {
        // Día 14 a las 21:00 → tarifa 1, vuelve a aplicar precio 35.50
        runTest("Test 3", "2020-06-14T21:00:00", 35455L, 1L, new BigDecimal("35.50"), 1L);
    }

    @Test
    void test4() throws Exception {
        // Día 15 a las 10:00 → tarifa 3, precio 30.50
        runTest("Test 4", "2020-06-15T10:00:00", 35455L, 1L, new BigDecimal("30.50"), 3L);
    }

    @Test
    void test5() throws Exception {
        // Día 16 a las 21:00 → tarifa 4, precio 38.95
        runTest("Test 5", "2020-06-16T21:00:00", 35455L, 1L, new BigDecimal("38.95"), 4L);
    }
}
