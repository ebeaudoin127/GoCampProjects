// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/UpdateCampsiteUnavailabilityRequest.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO modification d’une période d’indisponibilité de site
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.time.LocalDate;

public record UpdateCampsiteUnavailabilityRequest(
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        Boolean isBlocking,
        String notes
) {}