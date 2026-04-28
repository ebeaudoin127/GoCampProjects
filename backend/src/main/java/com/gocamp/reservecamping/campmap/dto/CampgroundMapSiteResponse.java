// ============================================================
// Fichier : CampgroundMapSiteResponse.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campmap/dto
// Dernière modification : 2026-04-18
//
// Résumé :
// - DTO site pour la carte du camping
// - Ajout du compteur de photos du site
// ============================================================

package com.gocamp.reservecamping.campmap.dto;

import java.util.List;

public record CampgroundMapSiteResponse(
        Long id,
        String siteCode,
        Boolean isActive,
        Integer labelX,
        Integer labelY,
        Integer photoCount,
        List<CampgroundMapPointResponse> polygon
) {}
