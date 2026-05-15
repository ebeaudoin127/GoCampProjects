// ============================================================
// Fichier : SearchCampsitesResultsPage.jsx
// Chemin  : frontend/src/pages/reservation
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Affiche la liste complète des terrains disponibles
// - Lit les résultats depuis sessionStorage
// - Pagination de 40 terrains par page
// - Regroupe visuellement les terrains par camping
// - Supporte le filtre par campgroundId dans l’URL
// - Supporte selectedCampsiteId dans l’URL
// - Charge la carte du camping avec la même source que l’écran d’édition
// - Utilise backgroundImagePath pour afficher l’image de fond
// - Affiche les terrains disponibles sur la carte
// - Les terrains disponibles clignotent en vert foncé
// - Permet de sélectionner un terrain depuis la carte ou la liste
// - Affiche les détails complets du terrain :
//   services, ampérages, dimensions, accès direct et surface
// - Sauvegarde le terrain sélectionné dans sessionStorage
// - Redirige vers la page de confirmation de réservation
//
// Historique des modifications :
// 2026-05-09
// - Création initiale de la page
//
// 2026-05-14
// - Ajout filtre URL campgroundId
// - Ajout sélection URL selectedCampsiteId
// - Ajout carte du camping dans la page des terrains disponibles
// - Ajout surbrillance vert foncé clignotante des terrains disponibles
// - Ajout synchronisation sélection liste/carte
// - Correction chargement image de carte avec backgroundImagePath
// - Ajout détails complets dans la liste des terrains
// - Ajout navigation vers /reservation-test/confirmation
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import {
  ArrowLeft,
  ChevronLeft,
  ChevronRight,
  MapPinned,
} from "lucide-react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import api from "../../services/api";

const STORAGE_KEY = "gocamp_search_summary";
const SELECTED_CAMPSITE_KEY = "gocamp_selected_campsite";
const PAGE_SIZE = 40;

function sortSitesByCode(sites) {
  return [...(Array.isArray(sites) ? sites : [])].sort((a, b) =>
    String(a?.siteCode ?? "").localeCompare(
      String(b?.siteCode ?? ""),
      "fr-CA",
      {
        numeric: true,
        sensitivity: "base",
      }
    )
  );
}

function getPolygonPoints(points) {
  if (!Array.isArray(points)) {
    return "";
  }

  return points.map((point) => `${point.x},${point.y}`).join(" ");
}

function getSiteCenter(site) {
  if (
    Number.isFinite(Number(site?.labelX)) &&
    Number.isFinite(Number(site?.labelY))
  ) {
    return {
      x: Number(site.labelX),
      y: Number(site.labelY),
    };
  }

  if (Array.isArray(site?.polygon) && site.polygon.length > 0) {
    const sum = site.polygon.reduce(
      (acc, point) => ({
        x: acc.x + Number(point.x || 0),
        y: acc.y + Number(point.y || 0),
      }),
      { x: 0, y: 0 }
    );

    return {
      x: Math.round(sum.x / site.polygon.length),
      y: Math.round(sum.y / site.polygon.length),
    };
  }

  return null;
}

function formatFeet(value) {
  if (value === null || value === undefined || value === "") {
    return "N/D";
  }

  return `${value} pi`;
}

function formatSurfaceValues(surfaceValues) {
  if (!surfaceValues) {
    return "Non spécifiée";
  }

  return String(surfaceValues)
    .split(";;")
    .map((item) => {
      const parts = item.split("|");
      return parts[1] || parts[0];
    })
    .filter(Boolean)
    .join(", ");
}

export default function SearchCampsitesResultsPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const campgroundIdParam = searchParams.get("campgroundId");
  const selectedCampsiteIdParam = searchParams.get("selectedCampsiteId");

  const campgroundIdFilter = campgroundIdParam
    ? Number(campgroundIdParam)
    : null;

  const selectedCampsiteIdFromUrl = selectedCampsiteIdParam
    ? Number(selectedCampsiteIdParam)
    : null;

  const [page, setPage] = useState(1);
  const [mapData, setMapData] = useState(null);
  const [loadingMap, setLoadingMap] = useState(false);
  const [mapError, setMapError] = useState("");
  const [selectedCampsiteId, setSelectedCampsiteId] = useState(
    Number.isFinite(selectedCampsiteIdFromUrl)
      ? selectedCampsiteIdFromUrl
      : null
  );

  const searchSummary = useMemo(() => {
    try {
      const raw = sessionStorage.getItem(STORAGE_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch (err) {
      console.error("Erreur lecture résultats recherche :", err);
      return null;
    }
  }, []);

  const allCampsites = useMemo(() => {
    const values = Array.isArray(searchSummary?.allCampsites)
      ? searchSummary.allCampsites
      : [];

    if (!campgroundIdFilter) {
      return values;
    }

    return values.filter(
      (site) => Number(site.campgroundId) === Number(campgroundIdFilter)
    );
  }, [searchSummary, campgroundIdFilter]);

  const selectedCampsite = useMemo(() => {
    return (
      allCampsites.find(
        (site) => Number(site.campsiteId) === Number(selectedCampsiteId)
      ) || null
    );
  }, [allCampsites, selectedCampsiteId]);

  const availableCampsiteIds = useMemo(() => {
    return new Set(allCampsites.map((site) => Number(site.campsiteId)));
  }, [allCampsites]);

  const mapSites = useMemo(() => {
    return sortSitesByCode(Array.isArray(mapData?.sites) ? mapData.sites : []);
  }, [mapData]);

  const hasMap = !!campgroundIdFilter;

  const mapWidth = Number(mapData?.imageWidth || 1600);
  const mapHeight = Number(mapData?.imageHeight || 900);

  const backgroundImagePath = mapData?.backgroundImagePath || "";

  const totalPages = Math.max(1, Math.ceil(allCampsites.length / PAGE_SIZE));

  const currentPageItems = useMemo(() => {
    const start = (page - 1) * PAGE_SIZE;
    return allCampsites.slice(start, start + PAGE_SIZE);
  }, [allCampsites, page]);

  useEffect(() => {
    setPage(1);
  }, [campgroundIdFilter]);

  useEffect(() => {
    if (!campgroundIdFilter) {
      setMapData(null);
      setMapError("");
      return;
    }

    const loadMap = async () => {
      setLoadingMap(true);
      setMapError("");

      try {
        const data = await api.get(`/campgrounds/${campgroundIdFilter}/map`);
        setMapData(data || null);
      } catch (err) {
        console.error(err);
        setMapError("Impossible de charger la carte du camping.");
      } finally {
        setLoadingMap(false);
      }
    };

    loadMap();
  }, [campgroundIdFilter]);

  useEffect(() => {
    if (!selectedCampsiteIdFromUrl) {
      return;
    }

    setSelectedCampsiteId(selectedCampsiteIdFromUrl);
  }, [selectedCampsiteIdFromUrl]);

  const goToPage = (nextPage) => {
    if (nextPage < 1 || nextPage > totalPages) {
      return;
    }

    setPage(nextPage);
    window.scrollTo(0, 0);
  };

  const handleSelectSite = (site) => {
    const campsiteId = Number(site?.campsiteId || site?.id);

    if (!Number.isFinite(campsiteId)) {
      return;
    }

    const fullSite =
      allCampsites.find(
        (item) => Number(item.campsiteId) === Number(campsiteId)
      ) || site;

    setSelectedCampsiteId(campsiteId);

    sessionStorage.setItem(
      SELECTED_CAMPSITE_KEY,
      JSON.stringify(fullSite)
    );

    navigate("/reservation-test/confirmation");
  };

  const getAvailableResultForMapSite = (mapSite) => {
    return allCampsites.find(
      (site) => Number(site.campsiteId) === Number(mapSite.id)
    );
  };

  if (!searchSummary) {
    return (
      <div className="mx-auto max-w-6xl px-6 py-10">
        <button
          type="button"
          onClick={() => navigate("/reservation-test")}
          className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900"
        >
          <ArrowLeft className="h-4 w-4" />
          Retour à la recherche
        </button>

        <div className="mt-6 rounded-2xl border bg-white p-6 text-gray-600">
          Aucun résultat de recherche trouvé.
        </div>
      </div>
    );
  }

  let lastCampgroundIdOnPage = null;

  return (
    <div className="mx-auto max-w-7xl px-6 py-10">
      <style>
        {`
          @keyframes gocampAvailablePulse {
            0% {
              fill-opacity: 0.35;
              stroke-opacity: 0.85;
              filter: drop-shadow(0 0 0 rgba(20, 83, 45, 0));
            }

            50% {
              fill-opacity: 0.78;
              stroke-opacity: 1;
              filter: drop-shadow(0 0 10px rgba(20, 83, 45, 0.75));
            }

            100% {
              fill-opacity: 0.35;
              stroke-opacity: 0.85;
              filter: drop-shadow(0 0 0 rgba(20, 83, 45, 0));
            }
          }

          .gocamp-available-site {
            fill: #14532d;
            stroke: #052e16;
            stroke-width: 4;
            animation: gocampAvailablePulse 1.2s ease-in-out infinite;
            cursor: pointer;
          }

          .gocamp-selected-site {
            fill: #1d4ed8;
            fill-opacity: 0.85;
            stroke: #1e3a8a;
            stroke-width: 6;
            cursor: pointer;
          }

          .gocamp-unavailable-site {
            fill: rgba(148, 163, 184, 0.18);
            stroke: rgba(100, 116, 139, 0.28);
            stroke-width: 2;
          }
        `}
      </style>

      <button
        type="button"
        onClick={() => navigate("/reservation-test")}
        className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900"
      >
        <ArrowLeft className="h-4 w-4" />
        Retour à la recherche
      </button>

      <div className="mt-4 flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            Terrains disponibles
          </h1>

          <p className="mt-2 text-gray-600">
            {allCampsites.length} terrain(s) disponible(s)
            {campgroundIdFilter ? " pour ce camping" : ""} • page {page} sur{" "}
            {totalPages}
          </p>
        </div>

        {campgroundIdFilter && (
          <div className="rounded-xl border border-green-200 bg-green-50 px-4 py-3 text-sm text-green-800">
            Les terrains disponibles clignotent en vert foncé sur la carte.
          </div>
        )}
      </div>

      {hasMap && (
        <div className="mt-6 rounded-3xl border bg-white p-5 shadow-sm">
          <div className="mb-4 flex items-center gap-2">
            <MapPinned className="h-5 w-5 text-green-700" />

            <h2 className="text-xl font-bold text-gray-900">
              Carte des terrains disponibles
            </h2>
          </div>

          {loadingMap && (
            <div className="rounded-xl bg-gray-50 p-5 text-gray-600">
              Chargement de la carte...
            </div>
          )}

          {mapError && (
            <div className="rounded-xl border border-red-200 bg-red-50 p-5 text-red-700">
              {mapError}
            </div>
          )}

          {!loadingMap && !mapError && mapData && (
            <div className="overflow-auto rounded-2xl border bg-slate-100">
              <div className="relative mx-auto w-full min-w-[900px]">
                {backgroundImagePath ? (
                  <img
                    src={backgroundImagePath}
                    alt={`Carte ${mapData?.campgroundName || "du camping"}`}
                    className="block w-full select-none"
                  />
                ) : (
                  <div
                    className="flex items-center justify-center bg-slate-200 text-slate-500"
                    style={{ aspectRatio: `${mapWidth} / ${mapHeight}` }}
                  >
                    Aucune image de fond configurée pour ce camping.
                  </div>
                )}

                <svg
                  className="absolute inset-0 h-full w-full"
                  viewBox={`0 0 ${mapWidth} ${mapHeight}`}
                  preserveAspectRatio="none"
                >
                  {mapSites.map((mapSite) => {
                    const availableResult =
                      getAvailableResultForMapSite(mapSite);

                    const isAvailable = availableCampsiteIds.has(
                      Number(mapSite.id)
                    );

                    const isSelected =
                      Number(selectedCampsiteId) === Number(mapSite.id);

                    if (
                      !Array.isArray(mapSite.polygon) ||
                      mapSite.polygon.length < 3
                    ) {
                      return null;
                    }

                    return (
                      <polygon
                        key={mapSite.id}
                        points={getPolygonPoints(mapSite.polygon)}
                        className={
                          isSelected
                            ? "gocamp-selected-site"
                            : isAvailable
                              ? "gocamp-available-site"
                              : "gocamp-unavailable-site"
                        }
                        onClick={() =>
                          isAvailable &&
                          handleSelectSite({
                            ...availableResult,
                            campsiteId: mapSite.id,
                          })
                        }
                      />
                    );
                  })}

                  {mapSites.map((mapSite) => {
                    const isAvailable = availableCampsiteIds.has(
                      Number(mapSite.id)
                    );

                    if (!isAvailable) {
                      return null;
                    }

                    const center = getSiteCenter(mapSite);

                    if (!center) {
                      return null;
                    }

                    return (
                      <text
                        key={`label-${mapSite.id}`}
                        x={center.x}
                        y={center.y}
                        textAnchor="middle"
                        dominantBaseline="middle"
                        fontSize="24"
                        fontWeight="800"
                        fill="#ffffff"
                        stroke="#052e16"
                        strokeWidth="3"
                        paintOrder="stroke"
                        pointerEvents="none"
                      >
                        {mapSite.siteCode}
                      </text>
                    );
                  })}
                </svg>
              </div>
            </div>
          )}

          {selectedCampsite && (
            <div className="mt-4 rounded-2xl border border-blue-200 bg-blue-50 p-4 text-blue-900">
              <div className="font-bold">
                Terrain sélectionné : {selectedCampsite.siteCode}
              </div>

              <div className="mt-1 text-sm">
                Longueur max :{" "}
                {selectedCampsite.maxEquipmentLengthFeet
                  ? `${selectedCampsite.maxEquipmentLengthFeet} pi`
                  : "Non spécifié"}
              </div>
            </div>
          )}
        </div>
      )}

      <div className="mt-6 rounded-3xl border bg-white shadow-sm">
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

              const isSelected =
                Number(selectedCampsiteId) === Number(site.campsiteId);

              return (
                <div key={site.campsiteId}>
                  {shouldShowCampground && (
                    <div className="bg-gray-50 px-5 py-4">
                      <h2 className="text-xl font-bold text-gray-900">
                        {site.campgroundName}
                      </h2>

                      <p className="text-sm text-gray-600">
                        {site.distanceKm?.toFixed(1)} km
                      </p>
                    </div>
                  )}

                  <div
                    className={`flex flex-col gap-3 px-5 py-4 md:flex-row md:items-start md:justify-between ${
                      isSelected ? "bg-blue-50" : "bg-white"
                    }`}
                  >
                    <div className="flex-1">
                      <div className="flex flex-wrap items-center gap-3">
                        <div className="text-lg font-semibold text-gray-900">
                          Terrain {site.siteCode}
                        </div>

                        {site.pullThrough && (
                          <span className="rounded-full bg-blue-100 px-3 py-1 text-xs font-bold text-blue-700">
                            Accès direct
                          </span>
                        )}
                      </div>

                      <div className="mt-3 grid gap-3 lg:grid-cols-2">
                        <div className="rounded-xl bg-gray-50 p-3">
                          <div className="text-xs font-semibold uppercase tracking-wide text-gray-500">
                            Dimensions
                          </div>

                          <div className="mt-2 space-y-1 text-sm text-gray-700">
                            <div>
                              <span className="font-semibold">Largeur :</span>{" "}
                              {formatFeet(site.widthFeet)}
                            </div>

                            <div>
                              <span className="font-semibold">Longueur :</span>{" "}
                              {formatFeet(site.lengthFeet)}
                            </div>

                            <div>
                              <span className="font-semibold">
                                Longueur max équipement :
                              </span>{" "}
                              {formatFeet(site.maxEquipmentLengthFeet)}
                            </div>
                          </div>
                        </div>

                        <div className="rounded-xl bg-gray-50 p-3">
                          <div className="text-xs font-semibold uppercase tracking-wide text-gray-500">
                            Services et surface
                          </div>

                          <div className="mt-2 flex flex-wrap gap-2">
                            {site.hasWater && (
                              <span className="rounded-full bg-cyan-100 px-3 py-1 text-xs font-bold text-cyan-700">
                                Eau
                              </span>
                            )}

                            {site.hasElectricity && (
                              <span className="rounded-full bg-yellow-100 px-3 py-1 text-xs font-bold text-yellow-700">
                                Électricité
                              </span>
                            )}

                            {site.hasSewer && (
                              <span className="rounded-full bg-green-100 px-3 py-1 text-xs font-bold text-green-700">
                                Égout
                              </span>
                            )}

                            {site.has15_20Amp && (
                              <span className="rounded-full bg-orange-100 px-3 py-1 text-xs font-bold text-orange-700">
                                15/20 amp
                              </span>
                            )}

                            {site.has30Amp && (
                              <span className="rounded-full bg-orange-100 px-3 py-1 text-xs font-bold text-orange-700">
                                30 amp
                              </span>
                            )}

                            {site.has50Amp && (
                              <span className="rounded-full bg-red-100 px-3 py-1 text-xs font-bold text-red-700">
                                50 amp
                              </span>
                            )}

                            {!site.hasWater &&
                              !site.hasElectricity &&
                              !site.hasSewer &&
                              !site.has15_20Amp &&
                              !site.has30Amp &&
                              !site.has50Amp && (
                                <span className="rounded-full bg-gray-100 px-3 py-1 text-xs font-bold text-gray-600">
                                  Aucun service indiqué
                                </span>
                              )}
                          </div>

                          <div className="mt-3 text-sm text-gray-700">
                            <span className="font-semibold">Surface :</span>{" "}
                            {formatSurfaceValues(site.surfaceValues)}
                          </div>
                        </div>
                      </div>
                    </div>

                    <button
                      type="button"
                      onClick={() => handleSelectSite(site)}
                      className={`rounded-lg px-4 py-2 text-sm font-semibold ${
                        isSelected
                          ? "bg-blue-700 text-white hover:bg-blue-800"
                          : "bg-green-700 text-white hover:bg-green-800"
                      }`}
                    >
                      {isSelected ? "Sélectionné" : "Choisir"}
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

      {totalPages > 1 && (
        <div className="mt-6 flex flex-wrap items-center gap-2">
          <button
            type="button"
            onClick={() => goToPage(page - 1)}
            disabled={page === 1}
            className="inline-flex items-center gap-2 rounded-lg border px-4 py-2 text-sm font-semibold disabled:opacity-40"
          >
            <ChevronLeft className="h-4 w-4" />
            Précédent
          </button>

          {Array.from({ length: totalPages }).map((_, index) => {
            const pageNumber = index + 1;

            return (
              <button
                type="button"
                key={pageNumber}
                onClick={() => goToPage(pageNumber)}
                className={`rounded-lg border px-4 py-2 text-sm font-semibold ${
                  pageNumber === page
                    ? "border-blue-600 bg-blue-600 text-white"
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
            <ChevronRight className="h-4 w-4" />
          </button>
        </div>
      )}

      <div className="mt-6">
        <Link
          to="/reservation-test"
          className="inline-flex items-center gap-2 text-sm font-semibold text-blue-700 hover:text-blue-900"
        >
          <ArrowLeft className="h-4 w-4" />
          Modifier la recherche
        </Link>
      </div>
    </div>
  );
}