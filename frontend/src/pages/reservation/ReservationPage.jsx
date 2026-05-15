// ============================================================
// Fichier : ReservationPage.jsx
// Chemin  : frontend/src/pages/reservation
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Page temporaire de test réservation/recherche
// - Recherche selon équipement actif par défaut
// - Cache les critères de base quand l’équipement actif est utilisé
// - Affiche les critères manuels quand l’équipement actif est désactivé
// - Ajoute longueur équipement et extensions conducteur/passager
// - Résumé cliquable vers les pages de résultats complets
// - Sauvegarde du résultat dans sessionStorage
// - Sauvegarde les critères de recherche dans sessionStorage
//   pour la page de confirmation : dates, nuits, équipement,
//   extensions, services et ampérages
// - Affiche le nombre de terrains supplémentaires disponibles
//   sous l’aperçu de chaque camping
// - Corrige le positionnement du popup photo dans la liste des terrains
// - Le bouton "Voir tous les terrains" ouvre la page des terrains
//   disponibles filtrée sur le camping sélectionné
// - Le bouton "Choisir" ouvre la page des terrains disponibles
//   en mettant le terrain choisi en sélection
//
// Historique des modifications :
// 2026-05-06
// - Création initiale de la page
//
// 2026-05-09
// - Ajout recherche résumé /searchavailability/summary
// - Ajout filtres de base manuels
// - Ajout sessionStorage pour résultats
// - Ajout navigation vers résultats campings et terrains
//
// 2026-05-12
// - Ajout champ longueur de l’équipement en recherche manuelle
// - Ajout extensions côté conducteur/passager en recherche manuelle
// - Envoi de equipmentLengthFeet au backend
// - Ajout compteur de terrains supplémentaires sous chaque aperçu
// - Correction popup photos : overflow visible + positionnement top/right
//
// 2026-05-14
// - Ajout navigation "Voir tous les terrains" par camping
// - Ajout navigation "Choisir" vers la page de détails des terrains
// - Ajout searchCriteria dans sessionStorage pour confirmation
// ============================================================

import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import ReservationDateSelector from "../../components/reservation/ReservationDateSelector";
import ReservationAvailabilityPanel from "../../components/reservation/ReservationAvailabilityPanel";
import ReservationSummaryCard from "../../components/reservation/ReservationSummaryCard";
import SearchAdvancedFilters from "../../components/reservation/SearchAdvancedFilters";

import {
  checkAvailability,
  createReservation,
  searchAvailabilitySummary,
} from "../../services/reservationService";

const STORAGE_KEY = "gocamp_search_summary";

const emptyAdvancedFilters = {
  pullThroughOnly: false,
  surfaceTypes: [],
  campgroundServiceCodes: [],
  activityCodes: [],
};

function calculateNights(arrivalDate, departureDate) {
  if (!arrivalDate || !departureDate) {
    return null;
  }

  const start = new Date(`${arrivalDate}T00:00:00`);
  const end = new Date(`${departureDate}T00:00:00`);
  const diffMs = end.getTime() - start.getTime();
  const nights = Math.round(diffMs / (1000 * 60 * 60 * 24));

  return nights > 0 ? nights : null;
}

export default function ReservationPage() {
  const navigate = useNavigate();

  const userId = 11;
  const campsiteId = 1;

  const defaultLatitude = 46.8139;
  const defaultLongitude = -71.208;
  const defaultRadiusKm = 100;

  const [arrivalDate, setArrivalDate] = useState("");
  const [departureDate, setDepartureDate] = useState("");

  const [useEquipmentContext, setUseEquipmentContext] = useState(true);

  const [equipmentLengthFeet, setEquipmentLengthFeet] = useState("");
  const [hasDriverSideSlideOut, setHasDriverSideSlideOut] = useState(false);
  const [driverSideSlideOutCount, setDriverSideSlideOutCount] = useState("");
  const [hasPassengerSideSlideOut, setHasPassengerSideSlideOut] =
    useState(false);
  const [passengerSideSlideOutCount, setPassengerSideSlideOutCount] =
    useState("");

  const [requiresWater, setRequiresWater] = useState(false);
  const [requiresElectricity, setRequiresElectricity] = useState(false);
  const [requiresSewer, setRequiresSewer] = useState(false);
  const [requires15_20Amp, setRequires15_20Amp] = useState(false);
  const [requires30Amp, setRequires30Amp] = useState(false);
  const [requires50Amp, setRequires50Amp] = useState(false);

  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);
  const [advancedFilters, setAdvancedFilters] = useState(emptyAdvancedFilters);

  const [availability, setAvailability] = useState(null);
  const [searchSummary, setSearchSummary] = useState(null);
  const [reservation, setReservation] = useState(null);
  const [hoveredSite, setHoveredSite] = useState(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  function handleElectricityChange(checked) {
    setRequiresElectricity(checked);

    if (!checked) {
      setRequires15_20Amp(false);
      setRequires30Amp(false);
      setRequires50Amp(false);
    }
  }

  function buildSearchCriteria() {
    const nights = calculateNights(arrivalDate, departureDate);

    return {
      arrivalDate,
      departureDate,
      nights,

      latitude: defaultLatitude,
      longitude: defaultLongitude,
      radiusKm: defaultRadiusKm,

      userId,
      useEquipmentContext,

      equipmentLengthFeet:
        useEquipmentContext || equipmentLengthFeet === ""
          ? null
          : Number(equipmentLengthFeet),

      hasDriverSideSlideOut,
      driverSideSlideOutCount:
        useEquipmentContext || !hasDriverSideSlideOut
          ? 0
          : Number(driverSideSlideOutCount || 0),

      hasPassengerSideSlideOut,
      passengerSideSlideOutCount:
        useEquipmentContext || !hasPassengerSideSlideOut
          ? 0
          : Number(passengerSideSlideOutCount || 0),

      requiresWater: useEquipmentContext ? false : requiresWater,
      requiresElectricity: useEquipmentContext ? false : requiresElectricity,
      requiresSewer: useEquipmentContext ? false : requiresSewer,
      requires15_20Amp: useEquipmentContext ? false : requires15_20Amp,
      requires30Amp: useEquipmentContext ? false : requires30Amp,
      requires50Amp: useEquipmentContext ? false : requires50Amp,

      advancedFilters,
    };
  }

  function buildStoredSearchSummary(summary) {
    const searchCriteria = buildSearchCriteria();

    return {
      ...summary,
      arrivalDate,
      departureDate,
      nights: searchCriteria.nights,
      searchCriteria,
    };
  }

  function saveSearchSummary(summary) {
    sessionStorage.setItem(
      STORAGE_KEY,
      JSON.stringify(buildStoredSearchSummary(summary))
    );
  }

  function goToCampgroundsResults() {
    if (!searchSummary) return;

    saveSearchSummary(searchSummary);
    navigate("/reservation-test/results/campgrounds");
  }

  function goToCampsitesResults(campgroundId = null, selectedCampsiteId = null) {
    if (!searchSummary) return;

    saveSearchSummary(searchSummary);

    const params = new URLSearchParams();

    if (campgroundId) {
      params.set("campgroundId", String(campgroundId));
    }

    if (selectedCampsiteId) {
      params.set("selectedCampsiteId", String(selectedCampsiteId));
    }

    const queryString = params.toString();

    navigate(
      queryString
        ? `/reservation-test/results/campsites?${queryString}`
        : "/reservation-test/results/campsites"
    );
  }

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
      sessionStorage.removeItem(STORAGE_KEY);

      const searchCriteria = buildSearchCriteria();

      const result = await searchAvailabilitySummary({
        arrivalDate,
        departureDate,
        latitude: defaultLatitude,
        longitude: defaultLongitude,
        radiusKm: defaultRadiusKm,
        userId,
        useEquipmentContext,
        advancedFilters: {
          equipmentLengthFeet: searchCriteria.equipmentLengthFeet,
          driverSideSlideOutCount: searchCriteria.driverSideSlideOutCount,
          passengerSideSlideOutCount:
            searchCriteria.passengerSideSlideOutCount,

          requiresWater: searchCriteria.requiresWater,
          requiresElectricity: searchCriteria.requiresElectricity,
          requiresSewer: searchCriteria.requiresSewer,
          requires15_20Amp: searchCriteria.requires15_20Amp,
          requires30Amp: searchCriteria.requires30Amp,
          requires50Amp: searchCriteria.requires50Amp,

          ...advancedFilters,
        },
      });

      const storedResult = {
        ...result,
        arrivalDate,
        departureDate,
        nights: searchCriteria.nights,
        searchCriteria,
      };

      setSearchSummary(storedResult);
      sessionStorage.setItem(STORAGE_KEY, JSON.stringify(storedResult));
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
    advancedFilters.pullThroughOnly ||
    advancedFilters.surfaceTypes.length > 0 ||
    advancedFilters.campgroundServiceCodes.length > 0 ||
    advancedFilters.activityCodes.length > 0;

  return (
    <div className="max-w-5xl mx-auto p-6">
      <h1 className="text-3xl font-bold mb-2">Réservation</h1>

      <p className="text-gray-600 mb-6">
        Recherche les terrains disponibles selon tes dates, ton équipement et
        tes préférences.
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
              GoCamp utilise par défaut la longueur et les préférences de
              recherche de ton équipement actif. Décoche cette option si tu veux
              choisir manuellement les critères de base.
            </div>
          </div>
        </label>
      </div>

      {!useEquipmentContext && (
        <div className="mt-5 rounded-2xl border border-gray-200 bg-white p-5 shadow-sm">
          <h2 className="mb-2 text-xl font-bold">Critères de base</h2>

          <p className="mb-4 text-sm text-gray-600">
            Ces critères servent à trouver rapidement les terrains compatibles
            lorsque tu n’utilises pas ton équipement actif.
          </p>

          <div className="mb-6 rounded-xl border border-gray-200 bg-gray-50 p-4">
            <h3 className="mb-3 font-semibold text-gray-800">
              Équipement à valider
            </h3>

            <div className="grid gap-4 md:grid-cols-3">
              <div>
                <label className="mb-1 block text-sm font-medium text-gray-700">
                  Longueur de l’équipement
                </label>

                <input
                  type="number"
                  min="0"
                  step="1"
                  value={equipmentLengthFeet}
                  onChange={(e) => setEquipmentLengthFeet(e.target.value)}
                  placeholder="Ex. 37"
                  className="w-full rounded-lg border px-3 py-2"
                />

                <p className="mt-1 text-xs text-gray-500">
                  Longueur totale en pieds.
                </p>
              </div>

              <div>
                <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
                  <input
                    type="checkbox"
                    checked={hasDriverSideSlideOut}
                    onChange={(e) => {
                      setHasDriverSideSlideOut(e.target.checked);

                      if (!e.target.checked) {
                        setDriverSideSlideOutCount("");
                      }
                    }}
                  />
                  Extension côté conducteur
                </label>

                {hasDriverSideSlideOut && (
                  <input
                    type="number"
                    min="0"
                    step="1"
                    value={driverSideSlideOutCount}
                    onChange={(e) =>
                      setDriverSideSlideOutCount(e.target.value)
                    }
                    placeholder="Nombre"
                    className="mt-2 w-full rounded-lg border px-3 py-2"
                  />
                )}
              </div>

              <div>
                <label className="flex items-center gap-2 text-sm font-medium text-gray-700">
                  <input
                    type="checkbox"
                    checked={hasPassengerSideSlideOut}
                    onChange={(e) => {
                      setHasPassengerSideSlideOut(e.target.checked);

                      if (!e.target.checked) {
                        setPassengerSideSlideOutCount("");
                      }
                    }}
                  />
                  Extension côté passager
                </label>

                {hasPassengerSideSlideOut && (
                  <input
                    type="number"
                    min="0"
                    step="1"
                    value={passengerSideSlideOutCount}
                    onChange={(e) =>
                      setPassengerSideSlideOutCount(e.target.value)
                    }
                    placeholder="Nombre"
                    className="mt-2 w-full rounded-lg border px-3 py-2"
                  />
                )}
              </div>
            </div>
          </div>

          <div className="grid gap-6 md:grid-cols-2">
            <div>
              <h3 className="mb-3 font-semibold text-gray-800">
                Services requis sur le site
              </h3>

              <div className="space-y-2 text-sm">
                <label className="flex items-center gap-2">
                  <input
                    type="checkbox"
                    checked={requiresWater}
                    onChange={(e) => setRequiresWater(e.target.checked)}
                  />
                  Eau
                </label>

                <label className="flex items-center gap-2">
                  <input
                    type="checkbox"
                    checked={requiresElectricity}
                    onChange={(e) => handleElectricityChange(e.target.checked)}
                  />
                  Électricité
                </label>

                <label className="flex items-center gap-2">
                  <input
                    type="checkbox"
                    checked={requiresSewer}
                    onChange={(e) => setRequiresSewer(e.target.checked)}
                  />
                  Égout
                </label>
              </div>
            </div>

            <div>
              <h3 className="mb-3 font-semibold text-gray-800">
                Ampérage requis
              </h3>

              {!requiresElectricity && (
                <p className="mb-2 text-xs text-gray-500">
                  Coche d’abord Électricité pour choisir un ampérage.
                </p>
              )}

              <div className="space-y-2 text-sm">
                <label
                  className={`flex items-center gap-2 ${
                    !requiresElectricity ? "text-gray-400" : ""
                  }`}
                >
                  <input
                    type="checkbox"
                    disabled={!requiresElectricity}
                    checked={requires15_20Amp}
                    onChange={(e) => setRequires15_20Amp(e.target.checked)}
                  />
                  15/20 Amp
                </label>

                <label
                  className={`flex items-center gap-2 ${
                    !requiresElectricity ? "text-gray-400" : ""
                  }`}
                >
                  <input
                    type="checkbox"
                    disabled={!requiresElectricity}
                    checked={requires30Amp}
                    onChange={(e) => setRequires30Amp(e.target.checked)}
                  />
                  30 Amp
                </label>

                <label
                  className={`flex items-center gap-2 ${
                    !requiresElectricity ? "text-gray-400" : ""
                  }`}
                >
                  <input
                    type="checkbox"
                    disabled={!requiresElectricity}
                    checked={requires50Amp}
                    onChange={(e) => setRequires50Amp(e.target.checked)}
                  />
                  50 Amp
                </label>
              </div>
            </div>
          </div>
        </div>
      )}

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
            <h2 className="text-2xl font-bold mb-4">Résumé de recherche</h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <button
                type="button"
                onClick={goToCampgroundsResults}
                className="rounded-xl border border-green-200 bg-green-50 p-5 text-left hover:bg-green-100 transition"
              >
                <div className="text-sm text-green-700">
                  Campings avec au moins un terrain disponible
                </div>

                <div className="text-3xl font-bold text-green-800">
                  {searchSummary.totalCampgrounds}
                </div>

                <div className="mt-2 text-xs font-semibold text-green-700">
                  Cliquer pour voir la liste complète
                </div>
              </button>

              <button
                type="button"
                onClick={() => goToCampsitesResults()}
                className="rounded-xl border border-blue-200 bg-blue-50 p-5 text-left hover:bg-blue-100 transition"
              >
                <div className="text-sm text-blue-700">
                  Terrains disponibles au total
                </div>

                <div className="text-3xl font-bold text-blue-800">
                  {searchSummary.totalCampsites}
                </div>

                <div className="mt-2 text-xs font-semibold text-blue-700">
                  Cliquer pour voir tous les terrains
                </div>
              </button>
            </div>

            <div className="mt-4 text-sm text-gray-600">
              GoCamp affiche un aperçu rapide des terrains disponibles. Tu peux
              cliquer sur les totaux pour ouvrir les résultats complets.
            </div>
          </div>

          <div className="rounded-2xl border border-gray-200 bg-white shadow-sm overflow-visible">
            <div className="border-b bg-gray-50 px-5 py-4">
              <h3 className="text-xl font-bold">Résultats disponibles</h3>
            </div>

            <div className="divide-y">
              {searchSummary.campgrounds?.map((campground) => {
                const previewCount = campground.previewCampsites?.length || 0;
                const extraCount =
                  campground.availableCampsiteCount - previewCount;

                return (
                  <div key={campground.campgroundId} className="p-5">
                    <div className="mb-4 flex flex-col md:flex-row md:items-center md:justify-between gap-2">
                      <div>
                        <h4 className="text-lg font-bold">
                          {campground.campgroundName}
                        </h4>

                        <p className="text-sm text-gray-600">
                          {campground.distanceKm?.toFixed(1)} km •{" "}
                          {campground.availableCampsiteCount} terrain(s)
                          disponible(s)
                        </p>
                      </div>

                      <button
                        type="button"
                        onClick={() =>
                          goToCampsitesResults(campground.campgroundId)
                        }
                        className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-semibold hover:bg-gray-50"
                      >
                        Voir tous les terrains
                      </button>
                    </div>

                    <div className="rounded-lg border border-gray-200 overflow-visible">
                      <table className="min-w-full text-sm">
                        <thead className="bg-gray-100 text-gray-700">
                          <tr>
                            <th className="px-4 py-3 text-left">Terrain</th>
                            <th className="px-4 py-3 text-left">
                              Longueur max
                            </th>
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

                                {hoveredSite?.campsiteId ===
                                  site.campsiteId && (
                                  <div className="absolute right-0 top-full z-50 mt-2 w-72 rounded-xl border border-gray-200 bg-white p-3 shadow-2xl">
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
                                <button
                                  type="button"
                                  onClick={() =>
                                    goToCampsitesResults(
                                      campground.campgroundId,
                                      site.campsiteId
                                    )
                                  }
                                  className="rounded-lg bg-green-600 px-4 py-2 text-white text-sm font-semibold hover:bg-green-700"
                                >
                                  Choisir
                                </button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>

                      {extraCount > 0 && (
                        <div className="border-t bg-blue-50 px-4 py-3 text-sm font-medium text-blue-700">
                          + {extraCount} terrain(s) supplémentaire(s)
                          disponible(s) selon cette recherche.
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>
      )}

      <ReservationSummaryCard reservation={reservation} />
    </div>
  );
}