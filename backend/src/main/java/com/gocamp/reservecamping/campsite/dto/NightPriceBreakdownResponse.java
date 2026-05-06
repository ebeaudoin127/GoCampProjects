
// ============================================================
// Fichier : NightPriceBreakdownResponse.java
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - Détail du calcul de prix pour une nuit précise
// - Utilisé par le calculateur détaillé night-based
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record NightPriceBreakdownResponse(
        LocalDate date,
        String pricingRuleName,
        BigDecimal basePrice,
        BigDecimal discountAmount,
        BigDecimal finalPrice,
        String appliedPromotion
) {}