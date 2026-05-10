// ============================================================
// Fichier : SearchCampsitesResultsPage.jsx
// Chemin  : frontend/src/pages/reservation
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Affiche la liste complète des terrains disponibles
// - Lit les résultats depuis sessionStorage
// - Pagination de 40 terrains par page
// - Regroupe visuellement les terrains par camping
// - Réaffiche le nom du camping au début de chaque page,
//   même si le camping était déjà affiché sur la page précédente
//
// Historique des modifications :
// 2026-05-09
// - Création initiale de la page
// ============================================================

import React, { useMemo, useState } from "react";
import { ArrowLeft, ChevronLeft, ChevronRight, MapPinned } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";

const STORAGE_KEY = "gocamp_search_summary";
const PAGE_SIZE = 40;

export default function SearchCampsitesResultsPage() {
  const navigate = useNavigate();
  const [page, setPage] = useState(1);

  const searchSummary = useMemo(() => {
    try {
      const raw = sessionStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch (err) {
      console.error("Erreur lecture résultats recherche :", err);
      return null;
    }
  }, []);

  const allCampsites = Array.isArray(searchSummary?.allCampsites)
    ? searchSummary.allCampsites
    : [];

  const totalPages = Math.max(1, Math.ceil(allCampsites.length / PAGE_SIZE));

  const currentPageItems = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE;
    return allCampsites.slice(start, start + PAGE_SIZE);
  }, [allCampsites, page]);

  const goToPage = (nextPage) => {
    if (nextPage < 1 || nextPage > totalPages) return;
    setPage(nextPage);
    window.scrollTo(0, 0);
  };

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

  let lastCampgroundIdOnPage = null;

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
        <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
          <MapPinned className="w-6 h-6" />
        </div>

        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Terrains disponibles
          </h1>
          <p className="text-gray-600 mt-1">
            {allCampsites.length} terrain(s) disponible(s) • page {page} sur{" "}
            {totalPages}
          </p>
        </div>
      </div>

      <div className="mt-8 rounded-2xl border bg-white shadow-sm overflow-hidden">
        {currentPageItems.length === 0 ? (
          <div className="p-6 text-gray-600">Aucun terrain disponible.</div>
        ) : (
          <div className="divide-y">
            {currentPageItems.map((site) => {
              const shouldShowCampground =
                lastCampgroundIdOnPage !== site.campgroundId;

              if (shouldShowCampground) {
                lastCampgroundIdOnPage = site.campgroundId;
              }

              return (
                <div key={site.campsiteId}>
                  {shouldShowCampground && (
                    <div className="bg-gray-50 px-5 py-4">
                      <h2 className="text-lg font-bold text-gray-900">
                        {site.campgroundName}
                      </h2>
                      <p className="text-sm text-gray-600">
                        {site.distanceKm?.toFixed(1)} km
                      </p>
                    </div>
                  )}

                  <div className="ml-6 border-l border-gray-200 px-5 py-4">
                    <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                      <div>
                        <div className="font-semibold text-gray-900">
                          Terrain {site.siteCode}
                        </div>

                        <div className="text-sm text-gray-600">
                          Longueur max :{" "}
                          {site.maxEquipmentLengthFeet
                            ? `${site.maxEquipmentLengthFeet} pi`
                            : "Non spécifié"}
                        </div>
                      </div>

                      <button className="rounded-lg bg-green-600 px-4 py-2 text-white text-sm font-semibold hover:bg-green-700">
                        Choisir
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

      {totalPages > 1 && (
        <div className="mt-6 flex flex-wrap items-center justify-center gap-2">
          <button
            type="button"
            onClick={() => goToPage(page - 1)}
            disabled={page === 1}
            className="inline-flex items-center gap-2 rounded-lg border px-4 py-2 text-sm font-semibold disabled:opacity-40"
          >
            <ChevronLeft className="w-4 h-4" />
            Précédent
          </button>

          {Array.from({ length: totalPages }).map((_, index) => {
            const pageNumber = index + 1;

            return (
              <button
                key={pageNumber}
                type="button"
                onClick={() => goToPage(pageNumber)}
                className={`rounded-lg border px-4 py-2 text-sm font-semibold ${
                  pageNumber === page
                    ? "bg-blue-600 text-white border-blue-600"
                    : "bg-white text-gray-700 hover:bg-gray-50"
                }`}
              >
                {pageNumber}
              </button>
            );
          })}

          <button
            type="button"
            onClick={() => goToPage(page + 1)}
            disabled={page === totalPages}
            className="inline-flex items-center gap-2 rounded-lg border px-4 py-2 text-sm font-semibold disabled:opacity-40"
          >
            Suivant
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      )}
    </div>
  );
}