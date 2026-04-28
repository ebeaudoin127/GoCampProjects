// ============================================================
// Fichier : CampgroundRowResponse.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - DTO léger pour afficher une ligne dans une liste de campings
// ============================================================

package com.gocamp.reservecamping.campground.dto;

public class CampgroundRowResponse {

    private Long id;
    private String name;
    private String city;
    private Long countryId;
    private String countryName;
    private Long provinceStateId;
    private String provinceStateName;
    private String email;
    private String phoneMain;
    private Integer totalSites;
    private Boolean hasWifi;
    private Boolean isWinterCamping;
    private Boolean isActive;

    public CampgroundRowResponse(
            Long id,
            String name,
            String city,
            Long countryId,
            String countryName,
            Long provinceStateId,
            String provinceStateName,
            String email,
            String phoneMain,
            Integer totalSites,
            Boolean hasWifi,
            Boolean isWinterCamping,
            Boolean isActive
    ) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.countryId = countryId;
        this.countryName = countryName;
        this.provinceStateId = provinceStateId;
        this.provinceStateName = provinceStateName;
        this.email = email;
        this.phoneMain = phoneMain;
        this.totalSites = totalSites;
        this.hasWifi = hasWifi;
        this.isWinterCamping = isWinterCamping;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Long getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public Long getProvinceStateId() {
        return provinceStateId;
    }

    public String getProvinceStateName() {
        return provinceStateName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneMain() {
        return phoneMain;
    }

    public Integer getTotalSites() {
        return totalSites;
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
}