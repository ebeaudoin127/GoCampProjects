// ============================================================
// Fichier : ReservationAvailabilityPanel.jsx
// Chemin  : frontend/src/components/reservation
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Affichage de la disponibilité d’un terrain
// - Affiche disponible ou indisponible
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du composant
// ============================================================

import React from "react";

export default function ReservationAvailabilityPanel({
  availability,
  loading,
  error,
}) {
  if (loading) {
    return (
      <div className="mt-4 p-4 rounded-lg border bg-gray-50">
        Vérification de la disponibilité...
      </div>
    );
  }

  if (error) {
    return (
      <div className="mt-4 p-4 rounded-lg border border-red-300 bg-red-50 text-red-700">
        {error}
      </div>
    );
  }

  if (!availability) {
    return null;
  }

  return (
    <div
      className={`mt-4 p-4 rounded-lg border ${
        availability.available
          ? "border-green-300 bg-green-50 text-green-700"
          : "border-red-300 bg-red-50 text-red-700"
      }`}
    >
      {availability.message}
    </div>
  );
}