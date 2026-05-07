// ============================================================
// Fichier : ReservationStatus.java
// Chemin  : backend/src/main/java/com/gocamp/reservecamping/reservation/model
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Enum représentant les différents statuts d’une réservation
//
// Historique des modifications :
// 2026-05-06
// - Création initiale de l’enum
// ============================================================

package com.gocamp.reservecamping.reservation.model;

public enum ReservationStatus {

    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    EXPIRED
}
