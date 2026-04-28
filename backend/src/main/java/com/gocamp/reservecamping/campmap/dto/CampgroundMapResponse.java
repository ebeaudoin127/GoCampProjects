// ============================================================
// Fichier : CampgroundMapResponse.java
// Chemin : campmap/dto
// ============================================================

package com.gocamp.reservecamping.campmap.dto;

import java.util.List;

public record CampgroundMapResponse(
        Long campgroundId,
        String campgroundName,
        String backgroundImagePath,
        Integer imageWidth,
        Integer imageHeight,
        List<CampgroundMapSiteResponse> sites,
        List<CampgroundMapElementResponse> elements
) {}