

// ============================================================
// Fichier : ReservationService.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/reservation/service
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Service principal du module réservation
// - Création de réservation
// - Validation disponibilité
// - Vérification de disponibilité avant réservation
// - Validation compatibilité équipement
// - Liste des réservations utilisateur en DTO
// - Annulation réservation en DTO
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du service
// - Ajout logique anti double-réservation
// - Ajout validation équipement ↔ campsite
// - Ajout méthode checkAvailability()
// - Correction : retour DTO au lieu de retourner l’entité Reservation
// - Ajout campsiteName et campgroundName dans ReservationResponse
// - Correction : Campsite n’a pas getName(), utilisation de getSiteCode()
// ============================================================

package com.gocamp.reservecamping.reservation.service;

import com.gocamp.reservecamping.campsite.model.Campsite;
import com.gocamp.reservecamping.campsite.repository.CampsiteRepository;
import com.gocamp.reservecamping.equipmentcompatibility.service.EquipmentCompatibilityService;
import com.gocamp.reservecamping.reservation.dto.ReservationAvailabilityResponse;
import com.gocamp.reservecamping.reservation.dto.ReservationRequest;
import com.gocamp.reservecamping.reservation.dto.ReservationResponse;
import com.gocamp.reservecamping.reservation.model.Reservation;
import com.gocamp.reservecamping.reservation.model.ReservationStatus;
import com.gocamp.reservecamping.reservation.repository.ReservationRepository;
import com.gocamp.reservecamping.user.User;
import com.gocamp.reservecamping.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CampsiteRepository campsiteRepository;
    private final EquipmentCompatibilityService equipmentCompatibilityService;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            CampsiteRepository campsiteRepository,
            EquipmentCompatibilityService equipmentCompatibilityService
    ) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.campsiteRepository = campsiteRepository;
        this.equipmentCompatibilityService = equipmentCompatibilityService;
    }

    public ReservationResponse createReservation(ReservationRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));

        Campsite campsite = campsiteRepository.findById(request.getCampsiteId())
                .orElseThrow(() ->
                        new RuntimeException("Campsite introuvable"));

        validateDates(request.getArrivalDate(), request.getDepartureDate());

        boolean alreadyReserved =
                isAlreadyReserved(
                        campsite,
                        request.getArrivalDate(),
                        request.getDepartureDate()
                );

        if (alreadyReserved) {
            throw new RuntimeException(
                    "Le campsite est déjà réservé pour cette période"
            );
        }

        List<String> compatibilityIssues =
                equipmentCompatibilityService.validateCompatibility(
                        user,
                        campsite
                );

        if (!compatibilityIssues.isEmpty()) {
            throw new RuntimeException(
                    String.join(" | ", compatibilityIssues)
            );
        }

        int numberOfNights = (int) ChronoUnit.DAYS.between(
                request.getArrivalDate(),
                request.getDepartureDate()
        );

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setCampsite(campsite);
        reservation.setArrivalDate(request.getArrivalDate());
        reservation.setDepartureDate(request.getDepartureDate());
        reservation.setNumberOfNights(numberOfNights);
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation =
                reservationRepository.save(reservation);

        return toResponse(savedReservation);
    }

    public ReservationAvailabilityResponse checkAvailability(
            Long campsiteId,
            LocalDate arrivalDate,
            LocalDate departureDate
    ) {

        Campsite campsite = campsiteRepository.findById(campsiteId)
                .orElseThrow(() ->
                        new RuntimeException("Campsite introuvable"));

        validateDates(arrivalDate, departureDate);

        boolean alreadyReserved =
                isAlreadyReserved(campsite, arrivalDate, departureDate);

        ReservationAvailabilityResponse response =
                new ReservationAvailabilityResponse();

        response.setCampsiteId(campsite.getId());
        response.setArrivalDate(arrivalDate);
        response.setDepartureDate(departureDate);
        response.setAvailable(!alreadyReserved);

        if (alreadyReserved) {
            response.setMessage(
                    "Le terrain n’est pas disponible pour cette période."
            );
        } else {
            response.setMessage(
                    "Le terrain est disponible pour cette période."
            );
        }

        return response;
    }

    public List<ReservationResponse> getReservationsByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("Utilisateur introuvable"));

        return reservationRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ReservationResponse cancelReservation(Long reservationId) {

        Reservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(() ->
                                new RuntimeException("Réservation introuvable"));

        reservation.setStatus(ReservationStatus.CANCELLED);

        Reservation savedReservation =
                reservationRepository.save(reservation);

        return toResponse(savedReservation);
    }

    private void validateDates(
            LocalDate arrivalDate,
            LocalDate departureDate
    ) {

        if (arrivalDate == null || departureDate == null) {
            throw new RuntimeException(
                    "Les dates d’arrivée et de départ sont obligatoires"
            );
        }

        if (
                departureDate.isBefore(arrivalDate)
                        || departureDate.isEqual(arrivalDate)
        ) {
            throw new RuntimeException(
                    "La date de départ doit être après la date d’arrivée"
            );
        }
    }

    private boolean isAlreadyReserved(
            Campsite campsite,
            LocalDate arrivalDate,
            LocalDate departureDate
    ) {

        return reservationRepository
                .existsByCampsiteAndStatusInAndArrivalDateLessThanAndDepartureDateGreaterThan(
                        campsite,
                        List.of(
                                ReservationStatus.PENDING,
                                ReservationStatus.CONFIRMED
                        ),
                        departureDate,
                        arrivalDate
                );
    }

    private ReservationResponse toResponse(Reservation reservation) {

        ReservationResponse response = new ReservationResponse();

        response.setReservationId(reservation.getId());
        response.setUserId(reservation.getUser().getId());

        response.setCampsiteId(reservation.getCampsite().getId());
        response.setCampsiteName(reservation.getCampsite().getSiteCode());

        response.setCampgroundId(
                reservation.getCampsite()
                        .getCampground()
                        .getId()
        );

        response.setCampgroundName(
                reservation.getCampsite()
                        .getCampground()
                        .getName()
        );

        response.setArrivalDate(reservation.getArrivalDate());
        response.setDepartureDate(reservation.getDepartureDate());
        response.setNumberOfNights(reservation.getNumberOfNights());
        response.setStatus(reservation.getStatus());
        response.setCreatedAt(reservation.getCreatedAt());

        return response;
    }
}