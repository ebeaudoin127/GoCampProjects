// ============================================================
// Fichier : CampgroundReservationPeriodResponse.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - DTO sortant pour une période de réservation d'un camping
// ============================================================

package com.gocamp.reservecamping.campground.dto;

import java.time.LocalDate;

public record CampgroundReservationPeriodResponse(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Boolean active
) {
}