// ============================================================
// Fichier : CampgroundPromotionResponse.java
// Dernière modification : 2026-05-04
// ============================================================

package com.gocamp.reservecamping.campground.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CampgroundPromotionResponse(
        Long id,
        String title,
        String description,
        String promoCode,
        LocalDate startDate,
        LocalDate endDate,
        String discountType,
        BigDecimal discountValue,
        String conditionsText,
        Boolean isActive
) {}