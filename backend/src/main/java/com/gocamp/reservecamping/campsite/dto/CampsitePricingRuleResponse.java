// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CampsitePricingRuleResponse.java
// Dernière modification : 2026-04-24
//
// Résumé :
// - DTO réponse d’une règle tarifaire
// - Support dynamicBasePrice
// - Support daysOfWeek
// - Ajout minimumNights
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CampsitePricingRuleResponse(
        Long id,
        Long campgroundId,
        String targetType,
        Long campsiteId,
        String siteCode,
        Long pricingOptionId,
        String pricingOptionName,
        String pricingType,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal fixedPrice,
        BigDecimal dynamicBasePrice,
        BigDecimal dynamicMinPrice,
        BigDecimal dynamicMaxPrice,
        Integer minimumNights,
        String label,
        String notes,
        Boolean isActive,
        List<String> daysOfWeek
) {}