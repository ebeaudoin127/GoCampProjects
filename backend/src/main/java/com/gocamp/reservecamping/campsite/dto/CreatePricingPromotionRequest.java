// ============================================================
// Fichier : CreatePricingPromotionRequest.java
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - DTO pour créer une promotion tarifaire dynamique
// - Supporte les promotions simples, forfaitaires et marketing
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
        List<Long> campsiteIds,

        LocalDate startDate,
        LocalDate endDate,

        String name,
        String description,

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