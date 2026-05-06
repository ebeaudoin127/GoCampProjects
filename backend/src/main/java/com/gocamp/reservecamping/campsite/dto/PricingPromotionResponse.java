// ============================================================
// Fichier : PricingPromotionResponse.java
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - DTO retourné au frontend pour une promotion tarifaire dynamique
// - Inclut la cible, les montants, les conditions marketing et les jours
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
        String description,

        PricingTargetType targetType,
        PromotionApplicationMode applicationMode,
        PromotionType promotionType,

        Long campsiteId,
        Long pricingOptionId,
        List<Long> campsiteIds,

        LocalDate startDate,
        LocalDate endDate,

        BigDecimal fixedPrice,
        BigDecimal discountPercent,
        BigDecimal discountAmount,

        Integer buyNights,
        Integer payNights,

        Integer packageNights,
        BigDecimal packagePrice,

        Integer requiredConsecutiveWeekends,

        Integer minNights,
        Integer maxNights,

        Integer priority,
        Boolean combinable,
        Boolean isActive,

        String promoCode,
        Boolean requiresPromoCode,
        LocalDate bookingBeforeDate,
        Integer arrivalWithinDays,
        PricingDayOfWeek requiredArrivalDay,

        List<PricingDayOfWeek> days
) {}