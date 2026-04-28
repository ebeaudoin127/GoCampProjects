// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CreateCampsiteUnavailabilityRequest.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO création d’une période d’indisponibilité de site
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.time.LocalDate;

public record CreateCampsiteUnavailabilityRequest(
        Long campsiteId,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        Boolean isBlocking,
        String notes
) {}