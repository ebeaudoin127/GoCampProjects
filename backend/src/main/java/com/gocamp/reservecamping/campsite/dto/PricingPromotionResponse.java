// ============================================================
// Fichier : PricingPromotionResponse.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO retourné au frontend
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import com.gocamp.reservecamping.campsite.model.PricingDayOfWeek;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import com.gocamp.reservecamping.campsite.model.PromotionApplicationMode;
import com.gocamp.reservecamping.campsite.model.PromotionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PricingPromotionResponse(
        Long id,
        String name,
        PricingTargetType targetType,
        PromotionApplicationMode applicationMode,
        PromotionType promotionType,

        LocalDate startDate,
        LocalDate endDate,

        BigDecimal fixedPrice,
        BigDecimal discountPercent,

        Integer buyNights,
        Integer payNights,

        Integer priority,

        List<PricingDayOfWeek> days
) {}
