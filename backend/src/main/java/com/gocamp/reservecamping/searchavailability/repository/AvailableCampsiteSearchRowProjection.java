// ============================================================
// Fichier : AvailableCampsiteSearchRowProjection.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/repository
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Projection interne représentant une ligne de résultat
// - Combine campground + campsite + distance + photos + filtres
// - Ajout des services directs du site
// - Ajout des ampérages disponibles sur le site
// - Ajout des dimensions du terrain pour l’écran résultats
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
//
// 2026-05-10
// - Ajout hasWater / hasElectricity / hasSewer
// - Ajout has15_20Amp / has30Amp / has50Amp
// - Ajout campgroundServiceCodes
// - Ajout activityCodes
//
// 2026-05-14
// - Ajout widthFeet
// - Ajout lengthFeet
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

    BigDecimal getWidthFeet();

    BigDecimal getLengthFeet();

    BigDecimal getMaxEquipmentLengthFeet();

    String getPhotoUrl1();

    String getPhotoUrl2();

    String getPhotoUrl3();

    Boolean getPullThrough();

    String getServiceTypeCode();

    String getServiceTypeNameFr();

    String getSurfaceValues();

    Boolean getHasWater();

    Boolean getHasElectricity();

    Boolean getHasSewer();

    Boolean getHas15_20Amp();

    Boolean getHas30Amp();

    Boolean getHas50Amp();

    String getCampgroundServiceCodes();

    String getActivityCodes();
}