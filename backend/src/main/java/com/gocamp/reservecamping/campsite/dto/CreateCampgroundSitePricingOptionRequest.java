// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CreateCampgroundSitePricingOptionRequest.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO création d’une valeur de tarification pour un camping
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

public record CreateCampgroundSitePricingOptionRequest(
        Long campgroundId,
        String name,
        Boolean isActive
) {}