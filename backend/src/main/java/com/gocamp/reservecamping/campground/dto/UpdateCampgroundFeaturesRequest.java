// ============================================================
// Fichier : UpdateCampgroundFeaturesRequest.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - DTO pour sauvegarder les services / activités / hébergements
// ============================================================

package com.gocamp.reservecamping.campground.dto;

import java.util.List;

public record UpdateCampgroundFeaturesRequest(
        List<Long> serviceIds,
        List<Long> activityIds,
        List<Long> accommodationTypeIds
) {}