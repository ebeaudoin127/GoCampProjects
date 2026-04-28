// ============================================================
// Fichier : CampsitePhotoResponse.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/dto
// Dernière modification : 2026-04-18
//
// Résumé :
// - DTO de réponse pour les photos d'un site
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

public record CampsitePhotoResponse(
        Long id,
        Long campsiteId,
        String filePath,
        String thumbnailPath,
        Boolean isPrimary,
        Integer displayOrder,
        String captionFr,
        String captionEn,
        Boolean isActive
) {}