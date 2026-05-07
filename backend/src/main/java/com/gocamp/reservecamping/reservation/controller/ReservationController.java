// ============================================================
// Fichier : ReservationController.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/reservation/controller
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Controller REST du module réservation
// - Création réservation
// - Vérification disponibilité campsite
// - Liste des réservations utilisateur
// - Annulation réservation
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du controller
// - Ajout endpoint GET /availability
// - Correction : retour ReservationResponse au lieu de Reservation
// ============================================================

package com.gocamp.reservecamping.reservation.controller;

import com.gocamp.reservecamping.reservation.dto.ReservationAvailabilityResponse;
import com.gocamp.reservecamping.reservation.dto.ReservationRequest;
import com.gocamp.reservecamping.reservation.dto.ReservationResponse;
import com.gocamp.reservecamping.reservation.service.ReservationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService
    ) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest request
    ) {

        ReservationResponse response =
                reservationService.createReservation(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/availability")
    public ResponseEntity<ReservationAvailabilityResponse> checkAvailability(
            @RequestParam Long campsiteId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate arrivalDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate departureDate
    ) {

        return ResponseEntity.ok(
                reservationService.checkAvailability(
                        campsiteId,
                        arrivalDate,
                        departureDate
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByUser(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                reservationService.getReservationsByUser(userId)
        );
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(
            @PathVariable Long reservationId
    ) {

        return ResponseEntity.ok(
                reservationService.cancelReservation(reservationId)
        );
    }
}