// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/pricing/dto/PriceCalculationResponse.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Réponse détaillée du moteur de tarification
// - Inclut le total et le détail par nuit
// ============================================================

package com.gocamp.reservecamping.pricing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PriceCalculationResponse(
        BigDecimal total,
        List<DailyPrice> breakdown
) {
    public record DailyPrice(
            LocalDate date,
            BigDecimal price,
            String source,
            String label
    ) {}
}
