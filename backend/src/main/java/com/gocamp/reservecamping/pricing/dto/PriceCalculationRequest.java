// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/pricing/dto/PriceCalculationRequest.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Requête pour calculer un prix de séjour
// ============================================================

package com.gocamp.reservecamping.pricing.dto;

import java.time.LocalDate;

public record PriceCalculationRequest(
        Long campsiteId,
        LocalDate date,
        Integer nights
) {}