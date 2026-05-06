// ============================================================
// Fichier : CampsiteUnavailabilityRepository.java
// Dernière modification : 2026-05-05
//
// Résumé :
// - Repository des indisponibilités temporaires des sites
// - Permet de lister les indisponibilités d’un site
// - Permet de trouver les indisponibilités bloquantes qui chevauchent une période
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

    List<CampsiteUnavailability> findByCampsiteIdAndIsBlockingTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAscEndDateAsc(
            Long campsiteId,
            LocalDate requestedEndDate,
            LocalDate requestedStartDate
    );
}
