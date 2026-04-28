// ============================================================
// Fichier : backend/src/main/java/com/gocamp/reservecamping/campsite/repository/CampsiteUnavailabilityRepository.java
// Dernière modification : 2026-04-20
//
// Résumé :
// - Repository des indisponibilités temporaires des sites
// - Inclut une méthode utile pour la future logique de réservation
// ============================================================

package com.gocamp.reservecamping.campsite.repository;

import com.gocamp.reservecamping.campsite.model.CampsiteUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CampsiteUnavailabilityRepository extends JpaRepository<CampsiteUnavailability, Long> {

    List<CampsiteUnavailability> findByCampsiteIdOrderByStartDateAscEndDateAsc(Long campsiteId);

    boolean existsByCampsiteIdAndIsBlockingTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long campsiteId,
            LocalDate requestedEndDate,
            LocalDate requestedStartDate
    );
}