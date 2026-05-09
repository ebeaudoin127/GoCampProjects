// ============================================================
// Fichier : SearchAvailabilityRepositoryCustom.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/repository
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Interface custom du moteur de recherche disponibilité
// - Recherche géographique des campgrounds
// - Recherche des terrains disponibles pour le résumé
// - Support du filtre longueur équipement
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
//
// 2026-05-08
// - Ajout findAvailableCampsiteRows()
//
// 2026-05-09
// - Ajout equipmentLengthFeet dans findAvailableCampsiteRows()
// ============================================================

package com.gocamp.reservecamping.searchavailability.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SearchAvailabilityRepositoryCustom {

    List<NearbyCampgroundProjection> findNearbyCampgrounds(
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm
    );

    List<AvailableCampsiteSearchRowProjection> findAvailableCampsiteRows(
            LocalDate arrivalDate,
            LocalDate departureDate,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm,
            Long campgroundId,
            BigDecimal equipmentLengthFeet
    );
}