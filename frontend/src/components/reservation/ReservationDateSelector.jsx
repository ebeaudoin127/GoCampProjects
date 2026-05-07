// ============================================================
// Fichier : ReservationDateSelector.jsx
// Chemin  : frontend/src/components/reservation
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Sélection des dates de réservation
// - Permet de choisir arrivée et départ
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du composant
// ============================================================

import React from "react";

export default function ReservationDateSelector({
  arrivalDate,
  departureDate,
  onArrivalDateChange,
  onDepartureDateChange,
}) {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      {/* ARRIVÉE */}
      <div>
        <label className="block text-sm font-medium mb-2">
          Date d’arrivée
        </label>

        <input
          type="date"
          value={arrivalDate}
          onChange={(e) => onArrivalDateChange(e.target.value)}
          className="w-full border rounded-lg px-3 py-2"
        />
      </div>

      {/* DÉPART */}
      <div>
        <label className="block text-sm font-medium mb-2">
          Date de départ
        </label>

        <input
          type="date"
          value={departureDate}
          onChange={(e) => onDepartureDateChange(e.target.value)}
          className="w-full border rounded-lg px-3 py-2"
        />
      </div>
    </div>
  );
}
