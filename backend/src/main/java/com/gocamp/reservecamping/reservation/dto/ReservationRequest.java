// ============================================================
// Fichier : ReservationRequest.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/reservation/dto
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO utilisé pour la création d’une réservation
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du DTO
// ============================================================

package com.gocamp.reservecamping.reservation.dto;

import java.time.LocalDate;

public class ReservationRequest {

    private Long userId;
    private Long campsiteId;

    private LocalDate arrivalDate;
    private LocalDate departureDate;

    // ========================================================
    // GETTERS
    // ========================================================

    public Long getUserId() {
        return userId;
    }

    public Long getCampsiteId() {
        return campsiteId;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    // ========================================================
    // SETTERS
    // ========================================================

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setCampsiteId(Long campsiteId) {
        this.campsiteId = campsiteId;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
