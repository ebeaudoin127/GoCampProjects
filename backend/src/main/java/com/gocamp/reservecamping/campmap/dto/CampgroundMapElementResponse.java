// ============================================================
// Fichier : CampgroundMapElementResponse.java
// Chemin : campmap/dto
// ============================================================

package com.gocamp.reservecamping.campmap.dto;

import java.util.List;

public record CampgroundMapElementResponse(
        Long id,
        String name,
        String typeName,
        Boolean isActive,
        Integer labelX,
        Integer labelY,
        List<CampgroundMapPointResponse> polygon
) {}