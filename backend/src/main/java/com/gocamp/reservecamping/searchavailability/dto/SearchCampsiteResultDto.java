// ============================================================
// Fichier : SearchCampsiteResultDto.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO représentant un terrain disponible dans la liste complète
// - Utilisé par la page résultats terrains avec pagination frontend
// - Ajoute les détails affichés dans SearchCampsitesResultsPage :
//   services directs, ampérages, dimensions, accès direct et surface
//
// Historique des modifications :
// 2026-05-09
// - Création initiale du DTO
//
// 2026-05-14
// - Ajout widthFeet
// - Ajout lengthFeet
// - Ajout pullThrough
// - Ajout surfaceValues
// - Ajout hasWater / hasElectricity / hasSewer
// - Ajout has15_20Amp / has30Amp / has50Amp
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

    private BigDecimal widthFeet;
    private BigDecimal lengthFeet;
    private BigDecimal maxEquipmentLengthFeet;

    private Boolean pullThrough = false;

    private String surfaceValues;

    private Boolean hasWater = false;
    private Boolean hasElectricity = false;
    private Boolean hasSewer = false;

    private Boolean has15_20Amp = false;
    private Boolean has30Amp = false;
    private Boolean has50Amp = false;

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

    public BigDecimal getWidthFeet() {
        return widthFeet;
    }

    public void setWidthFeet(BigDecimal widthFeet) {
        this.widthFeet = widthFeet;
    }

    public BigDecimal getLengthFeet() {
        return lengthFeet;
    }

    public void setLengthFeet(BigDecimal lengthFeet) {
        this.lengthFeet = lengthFeet;
    }

    public BigDecimal getMaxEquipmentLengthFeet() {
        return maxEquipmentLengthFeet;
    }

    public void setMaxEquipmentLengthFeet(BigDecimal maxEquipmentLengthFeet) {
        this.maxEquipmentLengthFeet = maxEquipmentLengthFeet;
    }

    public Boolean getPullThrough() {
        return pullThrough;
    }

    public void setPullThrough(Boolean pullThrough) {
        this.pullThrough = pullThrough;
    }

    public String getSurfaceValues() {
        return surfaceValues;
    }

    public void setSurfaceValues(String surfaceValues) {
        this.surfaceValues = surfaceValues;
    }

    public Boolean getHasWater() {
        return hasWater;
    }

    public void setHasWater(Boolean hasWater) {
        this.hasWater = hasWater;
    }

    public Boolean getHasElectricity() {
        return hasElectricity;
    }

    public void setHasElectricity(Boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public Boolean getHasSewer() {
        return hasSewer;
    }

    public void setHasSewer(Boolean hasSewer) {
        this.hasSewer = hasSewer;
    }

    public Boolean getHas15_20Amp() {
        return has15_20Amp;
    }

    public void setHas15_20Amp(Boolean has15_20Amp) {
        this.has15_20Amp = has15_20Amp;
    }

    public Boolean getHas30Amp() {
        return has30Amp;
    }

    public void setHas30Amp(Boolean has30Amp) {
        this.has30Amp = has30Amp;
    }

    public Boolean getHas50Amp() {
        return has50Amp;
    }

    public void setHas50Amp(Boolean has50Amp) {
        this.has50Amp = has50Amp;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }
}