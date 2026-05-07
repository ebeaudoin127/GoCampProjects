// ============================================================
// Fichier : ReservationSummaryCard.jsx
// Chemin  : frontend/src/components/reservation
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Carte résumé de réservation
// - Affiche les informations principales
//
// Historique des modifications :
// 2026-05-06
// - Création initiale du composant
// ============================================================

import React from "react";

export default function ReservationSummaryCard({
  reservation,
}) {
  if (!reservation) {
    return null;
  }

  return (
    <div className="mt-6 border rounded-xl p-5 bg-white shadow-sm">
      <h2 className="text-xl font-semibold mb-4">
        Réservation créée
      </h2>

      <div className="space-y-2 text-sm">
        <div>
          <strong>Camping :</strong> {reservation.campgroundName}
        </div>

        <div>
          <strong>Terrain :</strong> {reservation.campsiteName}
        </div>

        <div>
          <strong>Arrivée :</strong> {reservation.arrivalDate}
        </div>

        <div>
          <strong>Départ :</strong> {reservation.departureDate}
        </div>

        <div>
          <strong>Nombre de nuits :</strong>{" "}
          {reservation.numberOfNights}
        </div>

        <div>
          <strong>Statut :</strong> {reservation.status}
        </div>
      </div>
    </div>
  );
}