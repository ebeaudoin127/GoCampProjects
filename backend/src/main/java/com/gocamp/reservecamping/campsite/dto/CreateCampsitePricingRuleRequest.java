// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CreateCampsitePricingRuleRequest.java
// Dernière modification : 2026-04-24
//
// Résumé :
// - DTO création d’une règle tarifaire
// - Supporte FIXED / DYNAMIC
// - Supporte les jours applicables
// - Ajout du minimum de nuits obligatoire
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateCampsitePricingRuleRequest(
        Long campgroundId,
        String targetType,
        Long campsiteId,
        Long pricingOptionId,
        String pricingType,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal fixedPrice,
        BigDecimal dynamicMinPrice,
        BigDecimal dynamicBasePrice,
        BigDecimal dynamicMaxPrice,
        Integer minimumNights,
        String label,
        String notes,
        Boolean isActive,
        Boolean forceAdjustOverlaps,
        List<String> daysOfWeek
) {}
