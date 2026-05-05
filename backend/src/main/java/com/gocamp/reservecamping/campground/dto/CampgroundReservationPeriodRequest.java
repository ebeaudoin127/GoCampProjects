// ============================================================
// Fichier : CampgroundReservationPeriodRequest.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - DTO entrant pour une période de réservation d'un camping
// ============================================================

package com.gocamp.reservecamping.campground.dto;

import java.time.LocalDate;

public record CampgroundReservationPeriodRequest(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Boolean active
) {
}