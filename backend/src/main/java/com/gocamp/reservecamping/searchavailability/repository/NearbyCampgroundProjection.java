// ============================================================
// Fichier : NearbyCampgroundProjection.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/repository
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Projection pour les campgrounds proches
// - Utilisé par la recherche géographique
//
// Historique des modifications :
// 2026-05-07
// - Création initiale
// ============================================================

package com.gocamp.reservecamping.searchavailability.repository;

import java.math.BigDecimal;

public interface NearbyCampgroundProjection {

    Long getCampgroundId();

    String getCampgroundName();

    BigDecimal getGpsLatitude();

    BigDecimal getGpsLongitude();

    Double getDistanceKm();
}