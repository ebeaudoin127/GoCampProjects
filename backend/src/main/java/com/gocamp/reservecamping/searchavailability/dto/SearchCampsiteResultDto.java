// ============================================================
// Fichier : SearchCampsiteResultDto.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO représentant un terrain disponible dans la liste complète
// - Utilisé par la page résultats terrains avec pagination frontend
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchCampsiteResultDto {

    private Long campgroundId;
    private String campgroundName;
    private Double distanceKm;

    private Long campsiteId;
    private String siteCode;
    private BigDecimal maxEquipmentLengthFeet;

    private List<String> photoUrls = new ArrayList<>();

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

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
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

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }
}