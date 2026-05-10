// ============================================================
// Fichier : SearchAvailabilitySummaryResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO résumé de recherche disponibilité
// - Conserve l’aperçu 5 terrains par camping
// - Ajoute allCampsites pour la page complète des terrains
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchAvailabilitySummaryResponse {

    private int totalCampgrounds;
    private int totalCampsites;
    private int previewMaxRows;
    private int previewCampsitesPerCampground;

    private List<SearchCampgroundSummaryDto> campgrounds = new ArrayList<>();
    private List<SearchCampsiteResultDto> allCampsites = new ArrayList<>();

    public int getTotalCampgrounds() {
        return totalCampgrounds;
    }

    public void setTotalCampgrounds(int totalCampgrounds) {
        this.totalCampgrounds = totalCampgrounds;
    }

    public int getTotalCampsites() {
        return totalCampsites;
    }

    public void setTotalCampsites(int totalCampsites) {
        this.totalCampsites = totalCampsites;
    }

    public int getPreviewMaxRows() {
        return previewMaxRows;
    }

    public void setPreviewMaxRows(int previewMaxRows) {
        this.previewMaxRows = previewMaxRows;
    }

    public int getPreviewCampsitesPerCampground() {
        return previewCampsitesPerCampground;
    }

    public void setPreviewCampsitesPerCampground(int previewCampsitesPerCampground) {
        this.previewCampsitesPerCampground = previewCampsitesPerCampground;
    }

    public List<SearchCampgroundSummaryDto> getCampgrounds() {
        return campgrounds;
    }

    public void setCampgrounds(List<SearchCampgroundSummaryDto> campgrounds) {
        this.campgrounds = campgrounds;
    }

    public List<SearchCampsiteResultDto> getAllCampsites() {
        return allCampsites;
    }

    public void setAllCampsites(List<SearchCampsiteResultDto> allCampsites) {
        this.allCampsites = allCampsites;
    }
}
