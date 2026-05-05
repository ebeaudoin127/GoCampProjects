// ============================================================
// Fichier : PriceCalculationRequest.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Requête pour calculer un prix de séjour
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.time.LocalDate;

public record PriceCalculationRequest(
        Long campgroundId,
        Long campsiteId,
        LocalDate startDate,
        LocalDate endDate
) {}
