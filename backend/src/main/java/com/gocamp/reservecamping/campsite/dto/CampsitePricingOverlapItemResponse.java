// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CampsitePricingOverlapItemResponse.java
// Dernière modification : 2026-04-24
//
// Résumé :
// - DTO représentant une règle tarifaire qui chevauche
// - Support dynamicBasePrice
// - Support minimumNights
// - Support daysOfWeek
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CampsitePricingOverlapItemResponse(
        Long id,
        String targetType,
        String targetLabel,
        String pricingType,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal fixedPrice,
        BigDecimal dynamicBasePrice,
        BigDecimal dynamicMinPrice,
        BigDecimal dynamicMaxPrice,
        Integer minimumNights,
        String label,
        Boolean isActive,
        List<String> daysOfWeek
) {}