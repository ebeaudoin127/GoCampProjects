// ============================================================
// Fichier : ReservationPage.jsx
// Chemin  : frontend/src/pages/reservation
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Page temporaire de test réservation/recherche
// - Sélection des dates
// - Recherche résumé des disponibilités
// - Option pour utiliser ou non l’équipement actif
// - Ajout d’un panneau "+ de filtres"
//
// Historique des modifications :
// 2026-05-06
// - Création initiale de la page
//
// 2026-05-09
// - Ajout recherche résumé /searchavailability/summary
// - Ajout checkbox "Utiliser mon équipement actif"
// - Affichage des totaux campings/terrains
// - Affichage en liste compacte
// - Ajout panneau de filtres avancés
// ============================================================

import React, { useState } from "react";

import ReservationDateSelector from "../../components/reservation/ReservationDateSelector";
import ReservationAvailabilityPanel from "../../components/reservation/ReservationAvailabilityPanel";
import ReservationSummaryCard from "../../components/reservation/ReservationSummaryCard";
import SearchAdvancedFilters from "../../components/reservation/SearchAdvancedFilters";

import {
  checkAvailability,
  createReservation,
  searchAvailabilitySummary,
} from "../../services/reservationService";

const emptyAdvancedFilters = {
  requiresWater: false,
  requiresElectricity: false,
  requiresSewer: false,
  pullThroughOnly: false,
  surfaceTypes: [],
  activities: [],
};

export default function ReservationPage() {
  const userId = 11;
  const campsiteId = 1;

  const defaultLatitude = 46.8139;
  const defaultLongitude = -71.208;
  const defaultRadiusKm = 100;

  const [arrivalDate, setArrivalDate] = useState("");
  const [departureDate, setDepartureDate] = useState("");

  const [useEquipmentContext, setUseEquipmentContext] = useState(true);
  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);
  const [advancedFilters, setAdvancedFilters] = useState(emptyAdvancedFilters);

  const [availability, setAvailability] = useState(null);
  const [searchSummary, setSearchSummary] = useState(null);
  const [reservation, setReservation] = useState(null);

  const [hoveredSite, setHoveredSite] = useState(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

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

  async function handleSearchSummary() {
    try {
      setLoading(true);
      setError("");
      setAvailability(null);
      setReservation(null);
      setSearchSummary(null);

      const result = await searchAvailabilitySummary({
        arrivalDate,
        departureDate,
        latitude: defaultLatitude,
        longitude: defaultLongitude,
        radiusKm: defaultRadiusKm,
        userId,
        useEquipmentContext,
        advancedFilters,
      });

      setSearchSummary(result);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

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

  const hasActiveAdvancedFilters =
    advancedFilters.requiresWater ||
    advancedFilters.requiresElectricity ||
    advancedFilters.requiresSewer ||
    advancedFilters.pullThroughOnly ||
    advancedFilters.surfaceTypes.length > 0 ||
    advancedFilters.activities.length > 0;

  return (
    <div className="max-w-5xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-2">
        Réservation
      </h1>

      <p className="text-gray-600 mb-6">
        Recherche les terrains disponibles selon tes dates, ton équipement et tes préférences.
      </p>

      <ReservationDateSelector
        arrivalDate={arrivalDate}
        departureDate={departureDate}
        onArrivalDateChange={setArrivalDate}
        onDepartureDateChange={setDepartureDate}
      />

      <div className="mt-5 rounded-xl border border-blue-200 bg-blue-50 p-4">
        <label className="flex items-start gap-3">
          <input
            type="checkbox"
            checked={useEquipmentContext}
            onChange={(e) => setUseEquipmentContext(e.target.checked)}
            className="mt-1"
          />

          <div>
            <div className="font-semibold text-blue-900">
              Rechercher selon mon équipement actif
            </div>

            <div className="text-sm text-blue-700 mt-1">
              GoCamp filtre automatiquement les terrains trop courts pour ton équipement.
              Décoche cette option si tu veux explorer tous les terrains disponibles.
            </div>
          </div>
        </label>
      </div>

      <div className="mt-5">
        <button
          type="button"
          onClick={() => setShowAdvancedFilters((prev) => !prev)}
          className="rounded-lg border border-gray-300 bg-white px-4 py-2 font-semibold text-gray-700 hover:bg-gray-50"
        >
          {showAdvancedFilters ? "Masquer les filtres" : "+ de filtres"}
          {hasActiveAdvancedFilters ? " • filtres actifs" : ""}
        </button>
      </div>

      {showAdvancedFilters && (
        <SearchAdvancedFilters
          filters={advancedFilters}
          onChange={setAdvancedFilters}
        />
      )}

      <div className="flex flex-wrap gap-3 mt-6">
        <button
          onClick={handleSearchSummary}
          disabled={loading}
          className="bg-orange-600 hover:bg-orange-700 disabled:bg-orange-300 text-white px-5 py-2 rounded-lg"
        >
          Rechercher
        </button>

        <button
          onClick={handleCheckAvailability}
          disabled={loading}
          className="bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white px-5 py-2 rounded-lg"
        >
          Vérifier terrain test
        </button>

        <button
          onClick={handleCreateReservation}
          disabled={loading}
          className="bg-green-600 hover:bg-green-700 disabled:bg-green-300 text-white px-5 py-2 rounded-lg"
        >
          Réserver terrain test
        </button>
      </div>

      {loading && (
        <div className="mt-6 rounded-lg border bg-gray-50 p-4">
          Chargement...
        </div>
      )}

      <ReservationAvailabilityPanel
        availability={availability}
        loading={false}
        error={error}
      />

      {searchSummary && (
        <div className="mt-8 space-y-6">
          <div className="rounded-2xl border border-gray-200 bg-white p-5 shadow-sm">
            <h2 className="text-2xl font-bold mb-4">
              Résumé de recherche
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="rounded-xl border border-green-200 bg-green-50 p-5">
                <div className="text-sm text-green-700">
                  Campings avec au moins un terrain disponible
                </div>
                <div className="text-3xl font-bold text-green-800">
                  {searchSummary.totalCampgrounds}
                </div>
              </div>

              <div className="rounded-xl border border-blue-200 bg-blue-50 p-5">
                <div className="text-sm text-blue-700">
                  Terrains disponibles au total
                </div>
                <div className="text-3xl font-bold text-blue-800">
                  {searchSummary.totalCampsites}
                </div>
              </div>
            </div>

            <div className="mt-4 text-sm text-gray-600">
              GoCamp affiche un aperçu rapide des terrains disponibles. Tu pourras ensuite ouvrir un camping pour voir tous ses terrains.
            </div>
          </div>

          <div className="rounded-2xl border border-gray-200 bg-white shadow-sm overflow-hidden">
            <div className="border-b bg-gray-50 px-5 py-4">
              <h3 className="text-xl font-bold">
                Résultats disponibles
              </h3>
            </div>

            <div className="divide-y">
              {searchSummary.campgrounds?.map((campground) => (
                <div key={campground.campgroundId} className="p-5">
                  <div className="mb-4 flex flex-col md:flex-row md:items-center md:justify-between gap-2">
                    <div>
                      <h4 className="text-lg font-bold">
                        {campground.campgroundName}
                      </h4>

                      <p className="text-sm text-gray-600">
                        {campground.distanceKm?.toFixed(1)} km •{" "}
                        {campground.availableCampsiteCount} terrain(s) disponible(s)
                      </p>
                    </div>

                    <button className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-semibold hover:bg-gray-50">
                      Voir tous les terrains
                    </button>
                  </div>

                  <div className="rounded-lg border border-gray-200 overflow-hidden">
                    <table className="min-w-full text-sm">
                      <thead className="bg-gray-100 text-gray-700">
                        <tr>
                          <th className="px-4 py-3 text-left">Terrain</th>
                          <th className="px-4 py-3 text-left">Longueur max</th>
                          <th className="px-4 py-3 text-left">Photos</th>
                          <th className="px-4 py-3 text-right">Action</th>
                        </tr>
                      </thead>

                      <tbody className="divide-y bg-white">
                        {campground.previewCampsites?.map((site) => (
                          <tr
                            key={site.campsiteId}
                            className="hover:bg-orange-50"
                            onMouseEnter={() => setHoveredSite(site)}
                            onMouseLeave={() => setHoveredSite(null)}
                          >
                            <td className="px-4 py-3 font-semibold">
                              Terrain {site.siteCode}
                            </td>

                            <td className="px-4 py-3">
                              {site.maxEquipmentLengthFeet
                                ? `${site.maxEquipmentLengthFeet} pi`
                                : "Non spécifié"}
                            </td>

                            <td className="px-4 py-3 relative">
                              <span className="text-blue-600 underline cursor-help">
                                Voir photos
                              </span>

                              {hoveredSite?.campsiteId === site.campsiteId && (
                                <div className="absolute z-20 mt-2 w-72 rounded-xl border bg-white p-3 shadow-xl">
                                  <div className="text-sm font-semibold mb-2">
                                    Photos du terrain {site.siteCode}
                                  </div>

                                  <div className="grid grid-cols-3 gap-2">
                                    {site.photoUrls?.length > 0 ? (
                                      site.photoUrls.map((url) => (
                                        <img
                                          key={url}
                                          src={url}
                                          alt={`Terrain ${site.siteCode}`}
                                          className="h-20 w-full rounded object-cover"
                                        />
                                      ))
                                    ) : (
                                      <>
                                        <div className="h-20 rounded bg-gray-200 flex items-center justify-center text-xs text-gray-500">
                                          Aucune
                                        </div>
                                        <div className="h-20 rounded bg-gray-200 flex items-center justify-center text-xs text-gray-500">
                                          photo
                                        </div>
                                        <div className="h-20 rounded bg-gray-200 flex items-center justify-center text-xs text-gray-500">
                                          disponible
                                        </div>
                                      </>
                                    )}
                                  </div>
                                </div>
                              )}
                            </td>

                            <td className="px-4 py-3 text-right">
                              <button className="rounded-lg bg-green-600 px-4 py-2 text-white text-sm font-semibold hover:bg-green-700">
                                Choisir
                              </button>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      )}

      <ReservationSummaryCard reservation={reservation} />
    </div>
  );
}
