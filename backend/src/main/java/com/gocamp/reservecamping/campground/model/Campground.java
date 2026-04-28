// ============================================================
// Fichier : Campground.java
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Entité principale d'un camping
// - Reliée à country et province_state
// - Contient les infos générales, coordonnées, capacités et statut
// ============================================================

package com.gocamp.reservecamping.campground.model;

import com.gocamp.reservecamping.location.Country;
import com.gocamp.reservecamping.location.ProvinceState;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "campground",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_campground_name_city", columnNames = {"name", "city"})
        },
        indexes = {
                @Index(name = "idx_campground_name", columnList = "name"),
                @Index(name = "idx_campground_city", columnList = "city"),
                @Index(name = "idx_campground_active", columnList = "is_active"),
                @Index(name = "idx_campground_country", columnList = "country_id"),
                @Index(name = "idx_campground_province_state", columnList = "province_state_id")
        }
)
public class Campground {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informations générales
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Lob
    @Column(name = "long_description")
    private String longDescription;

    // Coordonnées
    @Column(name = "address_line_1", nullable = false, length = 150)
    private String addressLine1;

    @Column(name = "address_line_2", length = 150)
    private String addressLine2;

    @Column(name = "city", nullable = false, length = 80)
    private String city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_state_id", nullable = false)
    private ProvinceState provinceState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "postal_code", length = 15)
    private String postalCode;

    // Contact
    @Column(name = "phone_main", length = 20)
    private String phoneMain;

    @Column(name = "phone_secondary", length = 20)
    private String phoneSecondary;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "website", length = 255)
    private String website;

    // Localisation
    @Column(name = "gps_latitude", precision = 10, scale = 7)
    private BigDecimal gpsLatitude;

    @Column(name = "gps_longitude", precision = 10, scale = 7)
    private BigDecimal gpsLongitude;

    // Saison / périodes générales
    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    // Capacités / statistiques
    @Column(name = "total_sites")
    private Integer totalSites = 0;

    @Column(name = "sites_3_services")
    private Integer sites3Services = 0;

    @Column(name = "sites_2_services")
    private Integer sites2Services = 0;

    @Column(name = "sites_1_service")
    private Integer sites1Service = 0;

    @Column(name = "sites_no_service")
    private Integer sitesNoService = 0;

    @Column(name = "traveler_sites_count")
    private Integer travelerSitesCount = 0;

    // Infos additionnelles
    @Column(name = "shade_percentage")
    private Integer shadePercentage;

    @Column(name = "has_wifi", nullable = false)
    private Boolean hasWifi = false;

    @Column(name = "is_winter_camping", nullable = false)
    private Boolean isWinterCamping = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Audit
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relations
    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampgroundService> campgroundServices = new ArrayList<>();

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampgroundActivity> campgroundActivities = new ArrayList<>();

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampgroundAccommodationType> campgroundAccommodationTypes = new ArrayList<>();

    @OneToMany(mappedBy = "campground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampgroundPromotion> promotions = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.totalSites == null) this.totalSites = 0;
        if (this.sites3Services == null) this.sites3Services = 0;
        if (this.sites2Services == null) this.sites2Services = 0;
        if (this.sites1Service == null) this.sites1Service = 0;
        if (this.sitesNoService == null) this.sitesNoService = 0;
        if (this.travelerSitesCount == null) this.travelerSitesCount = 0;
        if (this.hasWifi == null) this.hasWifi = false;
        if (this.isWinterCamping == null) this.isWinterCamping = false;
        if (this.isActive == null) this.isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Campground() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ProvinceState getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(ProvinceState provinceState) {
        this.provinceState = provinceState;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneMain() {
        return phoneMain;
    }

    public void setPhoneMain(String phoneMain) {
        this.phoneMain = phoneMain;
    }

    public String getPhoneSecondary() {
        return phoneSecondary;
    }

    public void setPhoneSecondary(String phoneSecondary) {
        this.phoneSecondary = phoneSecondary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public BigDecimal getGpsLatitude() {
        return gpsLatitude;
    }

    public void setGpsLatitude(BigDecimal gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
    }

    public BigDecimal getGpsLongitude() {
        return gpsLongitude;
    }

    public void setGpsLongitude(BigDecimal gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getTotalSites() {
        return totalSites;
    }

    public void setTotalSites(Integer totalSites) {
        this.totalSites = totalSites;
    }

    public Integer getSites3Services() {
        return sites3Services;
    }

    public void setSites3Services(Integer sites3Services) {
        this.sites3Services = sites3Services;
    }

    public Integer getSites2Services() {
        return sites2Services;
    }

    public void setSites2Services(Integer sites2Services) {
        this.sites2Services = sites2Services;
    }

    public Integer getSites1Service() {
        return sites1Service;
    }

    public void setSites1Service(Integer sites1Service) {
        this.sites1Service = sites1Service;
    }

    public Integer getSitesNoService() {
        return sitesNoService;
    }

    public void setSitesNoService(Integer sitesNoService) {
        this.sitesNoService = sitesNoService;
    }

    public Integer getTravelerSitesCount() {
        return travelerSitesCount;
    }

    public void setTravelerSitesCount(Integer travelerSitesCount) {
        this.travelerSitesCount = travelerSitesCount;
    }

    public Integer getShadePercentage() {
        return shadePercentage;
    }

    public void setShadePercentage(Integer shadePercentage) {
        this.shadePercentage = shadePercentage;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public Boolean getIsWinterCamping() {
        return isWinterCamping;
    }

    public void setIsWinterCamping(Boolean winterCamping) {
        isWinterCamping = winterCamping;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<CampgroundService> getCampgroundServices() {
        return campgroundServices;
    }

    public void setCampgroundServices(List<CampgroundService> campgroundServices) {
        this.campgroundServices = campgroundServices;
    }

    public List<CampgroundActivity> getCampgroundActivities() {
        return campgroundActivities;
    }

    public void setCampgroundActivities(List<CampgroundActivity> campgroundActivities) {
        this.campgroundActivities = campgroundActivities;
    }

    public List<CampgroundAccommodationType> getCampgroundAccommodationTypes() {
        return campgroundAccommodationTypes;
    }

    public void setCampgroundAccommodationTypes(List<CampgroundAccommodationType> campgroundAccommodationTypes) {
        this.campgroundAccommodationTypes = campgroundAccommodationTypes;
    }

    public List<CampgroundPromotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<CampgroundPromotion> promotions) {
        this.promotions = promotions;
    }
}