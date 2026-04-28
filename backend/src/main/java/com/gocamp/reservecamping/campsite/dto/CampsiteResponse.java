// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CampsiteResponse.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO liste des sites d’un camping
// - Ajout de pricingOptionId et pricingOptionName
//   pour supporter la tarification par regroupement
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;

public record CampsiteResponse(
        Long id,
        String siteCode,
        String siteTypeName,
        String siteServiceTypeName,
        String siteAmperageName,
        BigDecimal widthFeet,
        BigDecimal lengthFeet,
        Boolean isPullThrough,
        Boolean isActive,
        Long pricingOptionId,
        String pricingOptionName
) {}
