// ============================================================
// Fichier : AvailableCampsiteDto.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO représentant un terrain disponible retourné par la recherche
// - Contient les infos du camping, du terrain et la distance
//
// Historique des modifications :
// 2026-05-07
// - Création initiale du DTO
// - Ajout campgroundId / campgroundName
// - Ajout campsiteId / siteCode
// - Ajout distanceKm
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.math.BigDecimal;

public class AvailableCampsiteDto {

    private Long campgroundId;
    private String campgroundName;

    private BigDecimal campgroundLatitude;
    private BigDecimal campgroundLongitude;

    private Long campsiteId;
    private String siteCode;

    private BigDecimal maxEquipmentLengthFeet;

    private BigDecimal distanceKm;

    public Long getCampgroundId() {
        return campgroundId;
    }

    public void setCampgroundId(Long campgroundId) {
        this.campgroundId = campgroundId;
    }

    public String getCampgroundName() {
        return campgroundName;
    }

    public void setCampgroundName(String campgroundName) {
        this.campgroundName = campgroundName;
    }

    public BigDecimal getCampgroundLatitude() {
        return campgroundLatitude;
    }

    public void setCampgroundLatitude(BigDecimal campgroundLatitude) {
        this.campgroundLatitude = campgroundLatitude;
    }

    public BigDecimal getCampgroundLongitude() {
        return campgroundLongitude;
    }

    public void setCampgroundLongitude(BigDecimal campgroundLongitude) {
        this.campgroundLongitude = campgroundLongitude;
    }

    public Long getCampsiteId() {
        return campsiteId;
    }

    public void setCampsiteId(Long campsiteId) {
        this.campsiteId = campsiteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public BigDecimal getMaxEquipmentLengthFeet() {
        return maxEquipmentLengthFeet;
    }

    public void setMaxEquipmentLengthFeet(BigDecimal maxEquipmentLengthFeet) {
        this.maxEquipmentLengthFeet = maxEquipmentLengthFeet;
    }

    public BigDecimal getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(BigDecimal distanceKm) {
        this.distanceKm = distanceKm;
    }
}
