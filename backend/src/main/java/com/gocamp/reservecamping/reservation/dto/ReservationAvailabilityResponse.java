// ============================================================
// Fichier : ReservationAvailabilityResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/reservation/dto
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO retourné lors d’une vérification de disponibilité
// - Indique si un campsite est disponible pour une période donnée
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du DTO
// ============================================================

package com.gocamp.reservecamping.reservation.dto;

import java.time.LocalDate;

public class ReservationAvailabilityResponse {

    private Long campsiteId;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private boolean available;
    private String message;

    public Long getCampsiteId() {
        return campsiteId;
    }

    public void setCampsiteId(Long campsiteId) {
        this.campsiteId = campsiteId;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}