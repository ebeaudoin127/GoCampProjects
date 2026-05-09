// ============================================================
// Fichier : reservationService.js
// Chemin  : frontend/src/services
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Service frontend du module réservation
// - Utilise api.js existant du projet
// - Vérifie disponibilité
// - Crée réservation
// - Charge réservations utilisateur
// - Annule réservation
// - Recherche résumé avec contexte équipement et filtres avancés
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du service réservation
// - Intégration avec api.js existant
//
// 2026-05-09
// - Ajout searchAvailabilitySummary()
// - Support de useEquipmentContext
// - Préparation des filtres avancés
// ============================================================

import api from "./api";

// ============================================================
// VÉRIFIER DISPONIBILITÉ
// ============================================================

export async function checkAvailability({
  campsiteId,
  arrivalDate,
  departureDate,
}) {
  const query =
    `/reservations/availability` +
    `?campsiteId=${campsiteId}` +
    `&arrivalDate=${arrivalDate}` +
    `&departureDate=${departureDate}`;

  return api.get(query);
}

// ============================================================
// CRÉER UNE RÉSERVATION
// ============================================================

export async function createReservation({
  userId,
  campsiteId,
  arrivalDate,
  departureDate,
}) {
  return api.post("/reservations", {
    userId,
    campsiteId,
    arrivalDate,
    departureDate,
  });
}

// ============================================================
// RÉSUMÉ DE RECHERCHE DISPONIBILITÉ
// ============================================================

export async function searchAvailabilitySummary({
  arrivalDate,
  departureDate,
  latitude,
  longitude,
  radiusKm,
  userId,
  useEquipmentContext,
  advancedFilters,
}) {
  return api.post("/searchavailability/summary", {
    arrivalDate,
    departureDate,
    latitude,
    longitude,
    radiusKm,
    userId,
    useEquipmentContext,
    ...advancedFilters,
  });
}

// ============================================================
// RÉSERVATIONS UTILISATEUR
// ============================================================

export async function getUserReservations(userId) {
  return api.get(`/reservations/user/${userId}`);
}

// ============================================================
// ANNULER RÉSERVATION
// ============================================================

export async function cancelReservation(reservationId) {
  return api.put(`/reservations/${reservationId}/cancel`, {});
}