// ============================================================
// Fichier : ReservationRepository.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/reservation/repository
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Repository JPA des réservations
// - Gestion des recherches et validations de disponibilité
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du repository
// - Ajout validation anti double-réservation
// ============================================================

package com.gocamp.reservecamping.reservation.repository;

import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.reservation.model.Reservation;
import com.gocamp.reservecamping.reservation.model.ReservationStatus;
import com.gocamp.reservecamping.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // ========================================================
    // RÉSERVATIONS D’UN UTILISATEUR
    // ========================================================

    List<Reservation> findByUser(User user);

    // ========================================================
    // RÉSERVATIONS D’UN CAMPSITE
    // ========================================================

    List<Reservation> findByCampsite(Campsite campsite);

    // ========================================================
    // VALIDATION DISPONIBILITÉ
    // ========================================================

    boolean existsByCampsiteAndStatusInAndArrivalDateLessThanAndDepartureDateGreaterThan(
            Campsite campsite,
            List<ReservationStatus> statuses,
            LocalDate departureDate,
            LocalDate arrivalDate
    );
}
