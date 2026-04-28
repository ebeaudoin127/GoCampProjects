// ============================================================
// Fichier : CampgroundFeaturesResponse.java
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - DTO contenant les IDs associés à un camping
// ============================================================

package com.gocamp.reservecamping.campground.dto;

import java.util.List;

public class CampgroundFeaturesResponse {

    private Long campgroundId;
    private List<Long> serviceIds;
    private List<Long> activityIds;
    private List<Long> accommodationTypeIds;

    public CampgroundFeaturesResponse(
            Long campgroundId,
            List<Long> serviceIds,
            List<Long> activityIds,
            List<Long> accommodationTypeIds
    ) {
        this.campgroundId = campgroundId;
        this.serviceIds = serviceIds;
        this.activityIds = activityIds;
        this.accommodationTypeIds = accommodationTypeIds;
    }

    public Long getCampgroundId() {
        return campgroundId;
    }

    public List<Long> getServiceIds() {
        return serviceIds;
    }

    public List<Long> getActivityIds() {
        return activityIds;
    }

    public List<Long> getAccommodationTypeIds() {
        return accommodationTypeIds;
    }
}