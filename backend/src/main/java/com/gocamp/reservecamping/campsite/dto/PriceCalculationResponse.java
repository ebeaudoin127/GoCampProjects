// ============================================================
// Fichier : PriceCalculationResponse.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Réponse du calculateur de prix
// - Indique disponibilité, message, total et détail par nuit
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.util.List;

public record PriceCalculationResponse(
        boolean available,
        String message,
        int nights,
        BigDecimal total,
        List<PriceCalculationLineResponse> lines
) {}