// ============================================================
// Fichier : PriceCalculationLineResponse.java
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceCalculationLineResponse(
        LocalDate date,
        String label,
        BigDecimal amount
) {}