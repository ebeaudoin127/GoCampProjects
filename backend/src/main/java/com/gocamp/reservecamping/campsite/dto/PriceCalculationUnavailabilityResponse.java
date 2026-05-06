// ============================================================
// Fichier : PriceCalculationUnavailabilityResponse.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - DTO représentant une indisponibilité détectée par le calculateur
// - Retourne la période, la raison et les notes
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.time.LocalDate;

public record PriceCalculationUnavailabilityResponse(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        String notes,
        boolean blocking
) {}
