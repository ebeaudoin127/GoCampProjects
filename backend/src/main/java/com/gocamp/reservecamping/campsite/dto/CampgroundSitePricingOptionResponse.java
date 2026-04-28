// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CampgroundSitePricingOptionResponse.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO réponse pour une valeur de tarification de site
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

public record CampgroundSitePricingOptionResponse(
        Long id,
        Long campgroundId,
        String name,
        Boolean isActive
) {}