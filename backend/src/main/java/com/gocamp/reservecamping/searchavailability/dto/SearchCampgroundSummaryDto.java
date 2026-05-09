// ============================================================
// Fichier : SearchCampgroundSummaryDto.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-08
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO résumé d’un camping dans les résultats de recherche
// - Contient la distance, le nombre de terrains disponibles
//   et un aperçu limité des premiers terrains disponibles
//
// Historique des modifications :
// 2026-05-08
// - Création initiale du DTO
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchCampgroundSummaryDto {

    private Long campgroundId;
    private String campgroundName;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private Double distanceKm;

    private int availableCampsiteCount;

    private List<SearchCampsitePreviewDto> previewCampsites =
            new ArrayList<>();

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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getAvailableCampsiteCount() {
        return availableCampsiteCount;
    }

    public void setAvailableCampsiteCount(int availableCampsiteCount) {
        this.availableCampsiteCount = availableCampsiteCount;
    }

    public List<SearchCampsitePreviewDto> getPreviewCampsites() {
        return previewCampsites;
    }

    public void setPreviewCampsites(
            List<SearchCampsitePreviewDto> previewCampsites
    ) {
        this.previewCampsites = previewCampsites;
    }
}