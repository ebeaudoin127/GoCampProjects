// ============================================================
// Fichier : AvailableCampsiteSearchRequest.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/searchavailability/dto
// Dernière modification : 2026-05-12
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO de requête pour rechercher des terrains disponibles
// - Supporte dates, GPS, rayon, campground précis, équipement
// - Supporte le contexte équipement actif
// - Supporte la longueur manuelle de l’équipement
// - Supporte les extensions côté conducteur/passager
// - Supporte les filtres de base : eau, électricité, égout,
//   ampérages requis
// - Supporte les filtres avancés : accès direct, surfaces,
//   services camping et activités
//
// Historique des modifications :
// 2026-05-07
// - Création initiale du DTO
//
// 2026-05-09
// - Ajout userId
// - Ajout useEquipmentContext activé par défaut
// - Ajout filtres avancés
// - Ajout requires15_20Amp / requires30Amp / requires50Amp
//
// 2026-05-12
// - Ajout driverSideSlideOutCount
// - Ajout passengerSideSlideOutCount
// - Conservation de equipmentLengthFeet pour recherche manuelle
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

    private Integer driverSideSlideOutCount = 0;
    private Integer passengerSideSlideOutCount = 0;

    private Boolean requiresWater = false;
    private Boolean requiresElectricity = false;
    private Boolean requiresSewer = false;

    private Boolean requires15_20Amp = false;
    private Boolean requires30Amp = false;
    private Boolean requires50Amp = false;

    private Boolean pullThroughOnly = false;

    private List<String> surfaceTypes = new ArrayList<>();
    private List<String> campgroundServiceCodes = new ArrayList<>();
    private List<String> activityCodes = new ArrayList<>();

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

    public Integer getDriverSideSlideOutCount() {
        return driverSideSlideOutCount;
    }

    public void setDriverSideSlideOutCount(Integer driverSideSlideOutCount) {
        this.driverSideSlideOutCount = driverSideSlideOutCount;
    }

    public Integer getPassengerSideSlideOutCount() {
        return passengerSideSlideOutCount;
    }

    public void setPassengerSideSlideOutCount(Integer passengerSideSlideOutCount) {
        this.passengerSideSlideOutCount = passengerSideSlideOutCount;
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

    public Boolean getRequires15_20Amp() {
        return requires15_20Amp;
    }

    public void setRequires15_20Amp(Boolean requires15_20Amp) {
        this.requires15_20Amp = requires15_20Amp;
    }

    public Boolean getRequires30Amp() {
        return requires30Amp;
    }

    public void setRequires30Amp(Boolean requires30Amp) {
        this.requires30Amp = requires30Amp;
    }

    public Boolean getRequires50Amp() {
        return requires50Amp;
    }

    public void setRequires50Amp(Boolean requires50Amp) {
        this.requires50Amp = requires50Amp;
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

    public List<String> getCampgroundServiceCodes() {
        return campgroundServiceCodes;
    }

    public void setCampgroundServiceCodes(List<String> campgroundServiceCodes) {
        this.campgroundServiceCodes = campgroundServiceCodes;
    }

    public List<String> getActivityCodes() {
        return activityCodes;
    }

    public void setActivityCodes(List<String> activityCodes) {
        this.activityCodes = activityCodes;
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