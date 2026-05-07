// ============================================================
// Fichier : ReservationResponse.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/reservation/dto
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - DTO retourné au frontend pour une réservation
// - Contient les informations principales de la réservation
// - Contient aussi des informations utiles du campsite
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du DTO response
// - Ajout campsiteName
// - Ajout campgroundId
// - Ajout campgroundName
// ============================================================

package com.gocamp.reservecamping.reservation.dto;

import com.gocamp.reservecamping.reservation.model.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationResponse {

    private Long reservationId;

    private Long userId;

    private Long campsiteId;
    private String campsiteName;

    private Long campgroundId;
    private String campgroundName;

    private LocalDate arrivalDate;
    private LocalDate departureDate;

    private Integer numberOfNights;

    private ReservationStatus status;

    private LocalDateTime createdAt;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCampsiteId() {
        return campsiteId;
    }

    public void setCampsiteId(Long campsiteId) {
        this.campsiteId = campsiteId;
    }

    public String getCampsiteName() {
        return campsiteName;
    }

    public void setCampsiteName(String campsiteName) {
        this.campsiteName = campsiteName;
    }

    public Long getCampgroundId() {
        return campgroundId;
    }

    public void setCampgroundId(Long campgroundId) {
        this.campgroundId = campgroundId;
    }

    public String getCampgroundName() {
        return campgroundName;
    }

    public void setCampgroundName(String campgroundName) {
        this.campgroundName = campgroundName;
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

    public Integer getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(Integer numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}