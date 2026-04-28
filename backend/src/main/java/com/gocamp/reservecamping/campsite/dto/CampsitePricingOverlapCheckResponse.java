// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CampsitePricingOverlapCheckResponse.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO réponse de vérification des chevauchements tarifaires
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.util.List;

public record CampsitePricingOverlapCheckResponse(
        Boolean hasOverlap,
        String message,
        List<CampsitePricingOverlapItemResponse> overlaps
) {}