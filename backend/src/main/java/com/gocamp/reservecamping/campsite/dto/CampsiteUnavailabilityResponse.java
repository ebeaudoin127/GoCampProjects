// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/dto/CampsiteUnavailabilityResponse.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - DTO réponse d’une période d’indisponibilité de site
// ============================================================

package com.gocamp.reservecamping.campsite.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CampsiteUnavailabilityResponse(
        Long id,
        Long campsiteId,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        Boolean isBlocking,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}