// ============================================================
// Fichier : CampgroundDetailsResponse.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - DTO détaillé pour afficher ou éditer un camping
// ============================================================

package com.gocamp.reservecamping.campground.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CampgroundDetailsResponse {

    private Long id;

    private String name;
    private String shortDescription;
    private String longDescription;

    private String addressLine1;
    private String addressLine2;
    private String city;

    private Long provinceStateId;
    private String provinceStateName;

    private Long countryId;
    private String countryName;

    private String postalCode;

    private String phoneMain;
    private String phoneSecondary;
    private String email;
    private String website;

    private BigDecimal gpsLatitude;
    private BigDecimal gpsLongitude;

    private LocalDate openingDate;
    private LocalDate closingDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    private Integer totalSites;
    private Integer sites3Services;
    private Integer sites2Services;
    private Integer sites1Service;
    private Integer sitesNoService;
    private Integer travelerSitesCount;

    private Integer shadePercentage;
    private Boolean hasWifi;
    private Boolean isWinterCamping;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CampgroundDetailsResponse(
            Long id,
            String name,
            String shortDescription,
            String longDescription,
            String addressLine1,
            String addressLine2,
            String city,
            Long provinceStateId,
            String provinceStateName,
            Long countryId,
            String countryName,
            String postalCode,
            String phoneMain,
            String phoneSecondary,
            String email,
            String website,
            BigDecimal gpsLatitude,
            BigDecimal gpsLongitude,
            LocalDate openingDate,
            LocalDate closingDate,
            LocalTime checkInTime,
            LocalTime checkOutTime,
            Integer totalSites,
            Integer sites3Services,
            Integer sites2Services,
            Integer sites1Service,
            Integer sitesNoService,
            Integer travelerSitesCount,
            Integer shadePercentage,
            Boolean hasWifi,
            Boolean isWinterCamping,
            Boolean isActive,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.provinceStateId = provinceStateId;
        this.provinceStateName = provinceStateName;
        this.countryId = countryId;
        this.countryName = countryName;
        this.postalCode = postalCode;
        this.phoneMain = phoneMain;
        this.phoneSecondary = phoneSecondary;
        this.email = email;
        this.website = website;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.totalSites = totalSites;
        this.sites3Services = sites3Services;
        this.sites2Services = sites2Services;
        this.sites1Service = sites1Service;
        this.sitesNoService = sitesNoService;
        this.travelerSitesCount = travelerSitesCount;
        this.shadePercentage = shadePercentage;
        this.hasWifi = hasWifi;
        this.isWinterCamping = isWinterCamping;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public Long getProvinceStateId() {
        return provinceStateId;
    }

    public String getProvinceStateName() {
        return provinceStateName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneMain() {
        return phoneMain;
    }

    public String getPhoneSecondary() {
        return phoneSecondary;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public BigDecimal getGpsLatitude() {
        return gpsLatitude;
    }

    public BigDecimal getGpsLongitude() {
        return gpsLongitude;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public Integer getTotalSites() {
        return totalSites;
    }

    public Integer getSites3Services() {
        return sites3Services;
    }

    public Integer getSites2Services() {
        return sites2Services;
    }

    public Integer getSites1Service() {
        return sites1Service;
    }

    public Integer getSitesNoService() {
        return sitesNoService;
    }

    public Integer getTravelerSitesCount() {
        return travelerSitesCount;
    }

    public Integer getShadePercentage() {
        return shadePercentage;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public Boolean getIsWinterCamping() {
        return isWinterCamping;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}