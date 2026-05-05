// ============================================================
// Fichier : CampgroundReservationPeriodRepository.java
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Repository des périodes de réservation d'un camping
// ============================================================

package com.gocamp.reservecamping.campground.repository;

import com.gocamp.reservecamping.campground.model.CampgroundReservationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CampgroundReservationPeriodRepository extends JpaRepository<CampgroundReservationPeriod, Long> {

    List<CampgroundReservationPeriod> findByCampgroundIdOrderByStartDateAscEndDateAsc(Long campgroundId);

    boolean existsByCampgroundIdAndActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long campgroundId,
            LocalDate endDate,
            LocalDate startDate
    );
}