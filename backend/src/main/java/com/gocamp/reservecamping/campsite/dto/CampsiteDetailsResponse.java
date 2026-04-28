// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CampsiteDetailsResponse.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO détail d’un site
// - Ajout du champ pricingOptionId et pricingOptionName
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.util.List;

public record CampsiteDetailsResponse(
        Long id,
        Long campgroundId,
        String siteCode,
        Long siteTypeId,
        String siteTypeName,
        Long siteServiceTypeId,
        String siteServiceTypeName,
        Long siteAmperageId,
        String siteAmperageName,
        BigDecimal widthFeet,
        BigDecimal lengthFeet,
        BigDecimal maxEquipmentLengthFeet,
        Boolean isPullThrough,
        Boolean isActive,
        String notes,
        List<Long> equipmentAllowedTypeIds,
        List<Long> siteSurfaceTypeIds,
        Long pricingOptionId,
        String pricingOptionName
) {}
