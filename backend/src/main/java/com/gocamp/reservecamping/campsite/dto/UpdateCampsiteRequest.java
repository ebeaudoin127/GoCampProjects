// ============================================================
// Fichier : UpdateCampsiteRequest.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/campsite/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO modification d’un site
// - Ajout services disponibles
// - Ajout ampérages disponibles
//
// Historique des modifications :
// 2026-04-20
// - Ajout du champ pricingOptionId
//
// 2026-05-09
// - Ajout hasWater / hasElectricity / hasSewer
// - Ajout has15_20Amp / has30Amp / has50Amp
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;
import java.util.List;

public record UpdateCampsiteRequest(
        String siteCode,
        Long siteTypeId,

        Long siteServiceTypeId,
        Long siteAmperageId,

        Boolean hasWater,
        Boolean hasElectricity,
        Boolean hasSewer,

        Boolean has15_20Amp,
        Boolean has30Amp,
        Boolean has50Amp,

        BigDecimal widthFeet,
        BigDecimal lengthFeet,
        BigDecimal maxEquipmentLengthFeet,
        Boolean isPullThrough,
        Boolean isActive,
        String notes,
        List<Long> equipmentAllowedTypeIds,
        List<Long> siteSurfaceTypeIds,
        Long pricingOptionId
) {
}
