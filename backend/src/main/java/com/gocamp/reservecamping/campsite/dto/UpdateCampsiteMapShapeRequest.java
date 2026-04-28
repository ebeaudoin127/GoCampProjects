// ============================================================
// Fichier : UpdateCampsiteMapShapeRequest.java
// Chemin : backend/src/main/java/com/gocamp/reservecamping/campsite/dto
// Dernière modification : 2026-04-18
//
// Résumé :
// - DTO pour sauvegarder le polygone d’un site
// - Sauvegarde aussi la position du label
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

public record UpdateCampsiteMapShapeRequest(
        String mapPolygonJson,
        Integer labelX,
        Integer labelY
) {}