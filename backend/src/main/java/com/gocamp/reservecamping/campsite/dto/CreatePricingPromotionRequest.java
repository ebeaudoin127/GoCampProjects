
// ============================================================
// Fichier : CreatePricingPromotionRequest.java
// Dernière modification : 2026-05-04
//
// Résumé :
// - DTO pour créer une promotion dynamique
// - Supporte les promotions par camping, regroupement, site unique ou multi-sites
// - Supporte %, montant fixe, prix fixe, nuits achetées/payées,
//   forfait X nuits pour X montant et fins de semaine consécutives
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

        List<PricingDayOfWeek> days
) {}