// ============================================================
// Fichier : AvailableCampsiteSearchRowProjection.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/repository
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Projection interne représentant une ligne de résultat
// - Combine campground + campsite + distance + photos + filtres
//
// Historique des modifications :
// 2026-05-08
// - Création initiale de la projection
//
// 2026-05-09
// - Ajout photoUrl1, photoUrl2, photoUrl3
// - Ajout pullThrough
// - Ajout serviceTypeCode / serviceTypeNameFr
// - Ajout surfaceValues
// ============================================================

package com.gocamp.reservecamping.searchavailability.repository;

import java.math.BigDecimal;

public interface AvailableCampsiteSearchRowProjection {

    Long getCampgroundId();

    String getCampgroundName();

    BigDecimal getGpsLatitude();

    BigDecimal getGpsLongitude();

    Double getDistanceKm();

    Long getCampsiteId();

    String getSiteCode();

    BigDecimal getMaxEquipmentLengthFeet();

    String getPhotoUrl1();

    String getPhotoUrl2();

    String getPhotoUrl3();

    Boolean getPullThrough();

    String getServiceTypeCode();

    String getServiceTypeNameFr();

    String getSurfaceValues();
}
