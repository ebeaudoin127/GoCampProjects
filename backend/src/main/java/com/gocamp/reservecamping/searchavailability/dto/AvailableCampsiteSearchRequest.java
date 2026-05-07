// ============================================================
// Fichier : AvailableCampsiteSearchRequest.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-07
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO de requête pour rechercher des terrains disponibles
// - Supporte les dates, la géolocalisation, le rayon,
//   un camping précis et les favoris futurs
//
// Historique des modifications :
// 2026-05-07
// - Création initiale du DTO
// - Ajout arrivalDate / departureDate
// - Ajout latitude / longitude / radiusKm
// - Ajout campgroundId optionnel
// - Ajout favoritesOnly pour fonctionnalité future
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AvailableCampsiteSearchRequest {

    private LocalDate arrivalDate;
    private LocalDate departureDate;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radiusKm;

    private Long campgroundId;

    private boolean favoritesOnly = false;

    private Integer page = 0;
    private Integer size = 20;

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
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

    public BigDecimal getRadiusKm() {
        return radiusKm;
    }

    public void setRadiusKm(BigDecimal radiusKm) {
        this.radiusKm = radiusKm;
    }

    public Long getCampgroundId() {
        return campgroundId;
    }

    public void setCampgroundId(Long campgroundId) {
        this.campgroundId = campgroundId;
    }

    public boolean isFavoritesOnly() {
        return favoritesOnly;
    }

    public void setFavoritesOnly(boolean favoritesOnly) {
        this.favoritesOnly = favoritesOnly;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
