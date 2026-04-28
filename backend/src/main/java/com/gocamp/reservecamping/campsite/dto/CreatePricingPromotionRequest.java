// ============================================================
// Fichier : CreatePricingPromotionRequest.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO pour créer une promotion ponctuelle
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import com.gocamp.reservecamping.campsite.model.PricingDayOfWeek;
import com.gocamp.reservecamping.campsite.model.PricingTargetType;
import com.gocamp.reservecamping.campsite.model.PromotionApplicationMode;
import com.gocamp.reservecamping.campsite.model.PromotionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreatePricingPromotionRequest(
        Long campgroundId,
        PricingTargetType targetType,
        PromotionApplicationMode applicationMode,
        PromotionType promotionType,

        Long campsiteId,
        Long pricingOptionId,

        LocalDate startDate,
        LocalDate endDate,

        String name,
        String description,

        BigDecimal fixedPrice,
        BigDecimal discountPercent,

        Integer buyNights,
        Integer payNights,

        Integer minNights,
        Integer priority,

        List<PricingDayOfWeek> days
) {}
