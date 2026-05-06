// ============================================================
// Fichier : AppliedPromotionResponse.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - DTO représentant une promotion appliquée au calculateur de prix
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;

public record AppliedPromotionResponse(
        Long id,
        String name,
        String promotionType,
        BigDecimal discountAmount,
        String description
) {}