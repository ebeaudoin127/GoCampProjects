// ============================================================
// Fichier : SearchAvailabilitySummaryResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-08
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO de réponse du résumé intelligent de recherche
// - Contient les totaux cliquables et un aperçu limité
//   des campings et terrains disponibles
//
// Historique des modifications :
// 2026-05-08
// - Création initiale du DTO
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchAvailabilitySummaryResponse {

    private int totalCampgrounds;
    private int totalCampsites;

    private int previewMaxRows = 40;
    private int previewCampsitesPerCampground = 5;

    private List<SearchCampgroundSummaryDto> campgrounds =
            new ArrayList<>();

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

    public void setPreviewCampsitesPerCampground(
            int previewCampsitesPerCampground
    ) {
        this.previewCampsitesPerCampground =
                previewCampsitesPerCampground;
    }

    public List<SearchCampgroundSummaryDto> getCampgrounds() {
        return campgrounds;
    }

    public void setCampgrounds(
            List<SearchCampgroundSummaryDto> campgrounds
    ) {
        this.campgrounds = campgrounds;
    }
}