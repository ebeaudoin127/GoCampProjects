// ============================================================
// Fichier : AvailableCampsiteSearchRequest.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO de requête pour rechercher des terrains disponibles
// - Supporte dates, GPS, rayon, campground précis, équipement
// - Supporte les filtres avancés : services, accès direct,
//   surfaces et activités futures
//
// Historique des modifications :
// 2026-05-07
// - Création initiale du DTO
//
// 2026-05-09
// - Ajout userId
// - Ajout useEquipmentContext activé par défaut
// - Ajout equipmentLengthFeet optionnel
// - Ajout filtres avancés
// ============================================================

package com.gocamp.reservecamping.searchavailability.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AvailableCampsiteSearchRequest {

    private LocalDate arrivalDate;
    private LocalDate departureDate;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radiusKm;

    private Long campgroundId;
    private Long userId;

    private Boolean useEquipmentContext = true;
    private BigDecimal equipmentLengthFeet;

    private Boolean requiresWater = false;
    private Boolean requiresElectricity = false;
    private Boolean requiresSewer = false;
    private Boolean pullThroughOnly = false;

    private List<String> surfaceTypes = new ArrayList<>();
    private List<String> activities = new ArrayList<>();

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getUseEquipmentContext() {
        return useEquipmentContext;
    }

    public void setUseEquipmentContext(Boolean useEquipmentContext) {
        this.useEquipmentContext = useEquipmentContext;
    }

    public BigDecimal getEquipmentLengthFeet() {
        return equipmentLengthFeet;
    }

    public void setEquipmentLengthFeet(BigDecimal equipmentLengthFeet) {
        this.equipmentLengthFeet = equipmentLengthFeet;
    }

    public Boolean getRequiresWater() {
        return requiresWater;
    }

    public void setRequiresWater(Boolean requiresWater) {
        this.requiresWater = requiresWater;
    }

    public Boolean getRequiresElectricity() {
        return requiresElectricity;
    }

    public void setRequiresElectricity(Boolean requiresElectricity) {
        this.requiresElectricity = requiresElectricity;
    }

    public Boolean getRequiresSewer() {
        return requiresSewer;
    }

    public void setRequiresSewer(Boolean requiresSewer) {
        this.requiresSewer = requiresSewer;
    }

    public Boolean getPullThroughOnly() {
        return pullThroughOnly;
    }

    public void setPullThroughOnly(Boolean pullThroughOnly) {
        this.pullThroughOnly = pullThroughOnly;
    }

    public List<String> getSurfaceTypes() {
        return surfaceTypes;
    }

    public void setSurfaceTypes(List<String> surfaceTypes) {
        this.surfaceTypes = surfaceTypes;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
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