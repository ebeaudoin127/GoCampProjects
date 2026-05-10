// ============================================================
// Fichier : CampsiteResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/campsite/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO liste des sites d’un camping
// - Ajout services disponibles
// - Ajout ampérages disponibles
//
// Historique des modifications :
// 2026-04-20
// - Ajout de pricingOptionId et pricingOptionName
//
// 2026-05-09
// - Ajout hasWater / hasElectricity / hasSewer
// - Ajout has15_20Amp / has30Amp / has50Amp
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.math.BigDecimal;

public record CampsiteResponse(
        Long id,
        String siteCode,

        String siteTypeName,
        String siteServiceTypeName,
        String siteAmperageName,

        Boolean hasWater,
        Boolean hasElectricity,
        Boolean hasSewer,

        Boolean has15_20Amp,
        Boolean has30Amp,
        Boolean has50Amp,

        BigDecimal widthFeet,
        BigDecimal lengthFeet,
        Boolean isPullThrough,
        Boolean isActive,
        Long pricingOptionId,
        String pricingOptionName
) {
}