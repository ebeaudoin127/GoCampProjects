// ============================================================
// Fichier : ReservationPage.jsx
// Chemin  : frontend/src/pages/reservation
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Page principale de réservation
// - Sélection des dates
// - Vérification disponibilité
// - Création réservation
//
// Historique des modifications :
// 2026-05-06
// - Création initiale de la page
// ============================================================

import React, { useState } from "react";

import ReservationDateSelector from "../../components/reservation/ReservationDateSelector";
import ReservationAvailabilityPanel from "../../components/reservation/ReservationAvailabilityPanel";
import ReservationSummaryCard from "../../components/reservation/ReservationSummaryCard";

import {
  checkAvailability,
  createReservation,
} from "../../services/reservationService";

export default function ReservationPage() {

  // ==========================================================
  // DEMO TEMPORAIRE
  // ==========================================================

  const userId = 11;
  const campsiteId = 1;

  // ==========================================================
  // STATES
  // ==========================================================

  const [arrivalDate, setArrivalDate] = useState("");
  const [departureDate, setDepartureDate] = useState("");

  const [availability, setAvailability] = useState(null);

  const [reservation, setReservation] = useState(null);

  const [loading, setLoading] = useState(false);

  const [error, setError] = useState("");

  // ==========================================================
  // VÉRIFIER DISPONIBILITÉ
  // ==========================================================

  async function handleCheckAvailability() {

    try {

      setLoading(true);
      setError("");
      setReservation(null);

      const result = await checkAvailability({
        campsiteId,
        arrivalDate,
        departureDate,
      });

      setAvailability(result);

    } catch (err) {

      setError(err.message);

    } finally {

      setLoading(false);
    }
  }

  // ==========================================================
  // CRÉER RÉSERVATION
  // ==========================================================

  async function handleCreateReservation() {

    try {

      setLoading(true);
      setError("");

      const result = await createReservation({
        userId,
        campsiteId,
        arrivalDate,
        departureDate,
      });

      setReservation(result);

    } catch (err) {

      setError(err.message);

    } finally {

      setLoading(false);
    }
  }

  return (
    <div className="max-w-3xl mx-auto p-6">

      {/* TITRE */}
      <h1 className="text-3xl font-bold mb-6">
        Réservation
      </h1>

      {/* DATES */}
      <ReservationDateSelector
        arrivalDate={arrivalDate}
        departureDate={departureDate}
        onArrivalDateChange={setArrivalDate}
        onDepartureDateChange={setDepartureDate}
      />

      {/* ACTIONS */}
      <div className="flex flex-wrap gap-3 mt-6">

        <button
          onClick={handleCheckAvailability}
          className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-lg"
        >
          Vérifier disponibilité
        </button>

        <button
          onClick={handleCreateReservation}
          className="bg-green-600 hover:bg-green-700 text-white px-5 py-2 rounded-lg"
        >
          Réserver
        </button>
      </div>

      {/* DISPONIBILITÉ */}
      <ReservationAvailabilityPanel
        availability={availability}
        loading={loading}
        error={error}
      />

      {/* RÉSUMÉ */}
      <ReservationSummaryCard
        reservation={reservation}
      />
    </div>
  );
}
