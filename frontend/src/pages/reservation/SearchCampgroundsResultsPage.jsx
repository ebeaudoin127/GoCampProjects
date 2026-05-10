// ============================================================
// Fichier : SearchCampgroundsResultsPage.jsx
// Chemin  : frontend/src/pages/reservation
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Affiche la liste complète des campings trouvés
// - Lit les résultats depuis sessionStorage
// - Tri conservé selon l’ordre retourné par la recherche
// - Retour possible vers la page de réservation
//
// Historique des modifications :
// 2026-05-09
// - Création initiale de la page
// ============================================================

import React, { useMemo } from "react";
import { ArrowLeft, MapPinned } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";

const STORAGE_KEY = "gocamp_search_summary";

export default function SearchCampgroundsResultsPage() {
  const navigate = useNavigate();

  const searchSummary = useMemo(() => {
    try {
      const raw = sessionStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch (err) {
      console.error("Erreur lecture résultats recherche :", err);
      return null;
    }
  }, []);

  const campgrounds = Array.isArray(searchSummary?.campgrounds)
    ? searchSummary.campgrounds
    : [];

  if (!searchSummary) {
    return (
      <div className="max-w-5xl mx-auto p-6">
        <button
          onClick={() => navigate("/reservation")}
          className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900"
        >
          <ArrowLeft className="w-4 h-4" />
          Retour à la recherche
        </button>

        <div className="mt-8 rounded-2xl border bg-white p-6 shadow-sm">
          Aucun résultat de recherche trouvé.
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto p-6">
      <Link
        to="/reservation"
        className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900"
      >
        <ArrowLeft className="w-4 h-4" />
        Retour à la recherche
      </Link>

      <div className="mt-6 flex items-center gap-3">
        <div className="rounded-2xl bg-green-100 p-3 text-green-700">
          <MapPinned className="w-6 h-6" />
        </div>

        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Campings disponibles
          </h1>
          <p className="text-gray-600 mt-1">
            {searchSummary.totalCampgrounds} camping(s) trouvé(s)
          </p>
        </div>
      </div>

      <div className="mt-8 space-y-4">
        {campgrounds.length === 0 ? (
          <div className="rounded-2xl border bg-white p-6 shadow-sm text-gray-600">
            Aucun camping disponible.
          </div>
        ) : (
          campgrounds.map((campground) => (
            <div
              key={campground.campgroundId}
              className="rounded-2xl border bg-white p-5 shadow-sm"
            >
              <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                <div>
                  <h2 className="text-xl font-bold text-gray-900">
                    {campground.campgroundName}
                  </h2>

                  <p className="text-sm text-gray-600 mt-1">
                    {campground.distanceKm?.toFixed(1)} km •{" "}
                    {campground.availableCampsiteCount} terrain(s) disponible(s)
                  </p>
                </div>

                <button className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-semibold hover:bg-gray-50">
                  Voir le camping
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}