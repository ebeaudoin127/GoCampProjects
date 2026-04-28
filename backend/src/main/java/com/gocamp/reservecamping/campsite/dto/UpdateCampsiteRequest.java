// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/UpdateCampsiteRequest.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO modification d’un site
// - Ajout du champ pricingOptionId pour la future tarification
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.util.List;

public record UpdateCampsiteRequest(
        String siteCode,
        Long siteTypeId,
        Long siteServiceTypeId,
        Long siteAmperageId,
        BigDecimal widthFeet,
        BigDecimal lengthFeet,
        BigDecimal maxEquipmentLengthFeet,
        Boolean isPullThrough,
        Boolean isActive,
        String notes,
        List<Long> equipmentAllowedTypeIds,
        List<Long> siteSurfaceTypeIds,
        Long pricingOptionId
) {}
