// ============================================================
// Fichier : PriceCalculationResponse.java
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - Réponse complète du calculateur de prix
// - Inclut disponibilité, prix de base, rabais, total final,
//   détail par nuit, promotions appliquées et indisponibilités
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.util.List;

public record PriceCalculationResponse(
        boolean available,
        String message,
        int nights,
        BigDecimal baseTotal,
        BigDecimal promotionDiscountTotal,
        BigDecimal total,
        List<PriceCalculationLineResponse> lines,
        List<NightPriceBreakdownResponse> nightBreakdown,
        List<AppliedPromotionResponse> appliedPromotions,
        List<PriceCalculationUnavailabilityResponse> unavailabilities
) {}