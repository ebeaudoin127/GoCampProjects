// ============================================================
// Fichier : CreateCampgroundRequest.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - DTO pour créer un camping
// - Inclut les périodes de réservation configurables
// ============================================================

package com.gocamp.reservecamping.campground.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateCampgroundRequest(

        String name,
        String shortDescription,
        String longDescription,

        String addressLine1,
        String addressLine2,
        String city,
        Long provinceStateId,
        Long countryId,
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

        List<CampgroundReservationPeriodRequest> reservationPeriods

) {
}
