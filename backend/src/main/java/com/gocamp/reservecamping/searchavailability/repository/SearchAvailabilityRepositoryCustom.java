// ============================================================
// Fichier : SearchAvailabilityRepositoryCustom.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/repository
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Interface custom du moteur de recherche disponibilité
// - Recherche géographique des campgrounds
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
// ============================================================

package com.gocamp.reservecamping.searchavailability.repository;

import java.math.BigDecimal;
import java.util.List;

public interface SearchAvailabilityRepositoryCustom {

    List<NearbyCampgroundProjection> findNearbyCampgrounds(
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal radiusKm
    );
}