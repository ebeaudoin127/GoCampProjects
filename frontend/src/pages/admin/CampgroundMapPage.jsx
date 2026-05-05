// ============================================================
// Fichier : frontend/src/pages/admin/CampgroundMapPage.jsx
// Dernière modification : 2026-04-29
//
// Résumé :
// - Page carte interactive
// - Supporte l’ouverture en contexte d’un site via ?siteId=
// - Une seule carte pour vue + sélection + édition
// - Ajout de 3 onglets : Tous les sites / Contour à dessiner / Photos à ajouter
// - Correction du tri naturel des codes de site : 1,2,10 / A101,A102
//
// ------------------------------------------------------------
// MODIFICATIONS (2026-04-29)
// - Ajout d’un bouton pour réinitialiser/supprimer le polygone en BD
// - Appel DELETE /campsites/{id}/map-shape
// - Vide le brouillon, quitte le mode édition et recharge la carte
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { ArrowLeft, Image as ImageIcon, List, RefreshCcw } from "lucide-react";
import { Link, useNavigate, useParams, useSearchParams } from "react-router-dom";
import api from "../../services/api";
import CampgroundMapViewer from "../../components/map/CampgroundMapViewer";
import MapLegend from "../../components/map/MapLegend";
import CampsitePhotoManager from "../../components/campsite/CampsitePhotoManager";

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

function computeCentroid(points) {
  if (!points.length) return { x: 0, y: 0 };

  const sum = points.reduce(
    (acc, p) => ({ x: acc.x + p.x, y: acc.y + p.y }),
    { x: 0, y: 0 }
  );

  return {
    x: Math.round(sum.x / points.length),
    y: Math.round(sum.y / points.length),
  };
}

function hasPolygon(site) {
  return Array.isArray(site?.polygon) && site.polygon.length >= 3;
}

function hasMaxPhotos(site) {
  return (site?.photoCount ?? 0) >= 3;
}

function isSiteComplete(site) {
  return hasPolygon(site) && hasMaxPhotos(site);
}

export default function CampgroundMapPage() {
  const { campgroundId } = useParams();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const [mapData, setMapData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [selectedSiteId, setSelectedSiteId] = useState(null);

  const [editMode, setEditMode] = useState(false);
  const [draftPoints, setDraftPoints] = useState([]);
  const [message, setMessage] = useState("");
  const [saving, setSaving] = useState(false);

  const [siteTab, setSiteTab] = useState("all");

  const loadMap = async () => {
    setLoading(true);
    setError("");

    try {
      const data = await api.get(`/campgrounds/${campgroundId}/map`);
      setMapData(data || null);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger la carte du camping.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMap();
  }, [campgroundId]);

  const sites = useMemo(() => {
    return sortSitesByCode(Array.isArray(mapData?.sites) ? mapData.sites : []);
  }, [mapData]);

  useEffect(() => {
    const siteIdParam = searchParams.get("siteId");
    if (!siteIdParam || sites.length === 0) return;

    const siteIdNumber = Number(siteIdParam);
    if (!Number.isFinite(siteIdNumber)) return;

    const site = sites.find((s) => s.id === siteIdNumber);
    if (site) {
      setSelectedSiteId(site.id);
    }
  }, [searchParams, sites]);

  const selectedSite = useMemo(() => {
    return sites.find((s) => s.id === selectedSiteId) || null;
  }, [sites, selectedSiteId]);

  const displayedSites = useMemo(() => {
    if (siteTab === "to-draw") {
      return sortSitesByCode(sites.filter((site) => !hasPolygon(site)));
    }

    if (siteTab === "photos") {
      return sortSitesByCode(sites.filter((site) => (site.photoCount ?? 0) < 3));
    }

    return sortSitesByCode(sites);
  }, [sites, siteTab]);

  const handleSiteClick = (site) => {
    setSelectedSiteId(site.id);
    setMessage("");

    if (editMode) {
      setDraftPoints(Array.isArray(site.polygon) ? site.polygon : []);
    }
  };

  const handleElementClick = (element) => {
    console.log("Élément de carte cliqué :", element);
  };

  const openSelectedSitePage = () => {
    if (!selectedSite) return;
    navigate(`/site-manager/campgrounds/${campgroundId}/sites/${selectedSite.id}/edit`);
  };

  const addDraftPoint = (point) => {
    if (!selectedSite) {
      setMessage("Sélectionne d’abord un site.");
      return;
    }

    if (!editMode) return;

    setDraftPoints((prev) => [...prev, point]);
    setMessage("");
  };

  const updateDraftPoint = (index, point) => {
    if (!editMode) return;

    setDraftPoints((prev) =>
      prev.map((existingPoint, currentIndex) =>
        currentIndex === index ? point : existingPoint
      )
    );

    setMessage("");
  };

  const deleteDraftPoint = (index) => {
    if (!editMode) return;

    setDraftPoints((prev) => prev.filter((_, currentIndex) => currentIndex !== index));
    setMessage("Point supprimé du brouillon.");
  };

  const startEdit = () => {
    if (!selectedSite) {
      setMessage("Sélectionne d’abord un site à éditer.");
      return;
    }

    setEditMode(true);
    setDraftPoints(Array.isArray(selectedSite.polygon) ? selectedSite.polygon : []);
    setMessage(
      Array.isArray(selectedSite.polygon) && selectedSite.polygon.length > 0
        ? "Mode édition activé. Le polygone existant a été chargé. Glisse un point rouge pour le modifier ou double-clique pour le supprimer."
        : "Mode édition activé. Clique sur la carte pour ajouter des points."
    );
  };

  const cancelEdit = () => {
    setEditMode(false);
    setDraftPoints([]);
    setMessage("");
  };

  const removeLastPoint = () => {
    setDraftPoints((prev) => prev.slice(0, -1));
  };

  const resetDraft = () => {
    setDraftPoints([]);
    setMessage("Brouillon réinitialisé. Le polygone en base de données n’a pas été supprimé.");
  };

  const reloadExistingPolygon = () => {
    if (!selectedSite) return;
    setDraftPoints(Array.isArray(selectedSite.polygon) ? selectedSite.polygon : []);
    setMessage("Polygone existant rechargé.");
  };

  const savePolygon = async () => {
    if (!selectedSite) {
      setMessage("Sélectionne un site avant de sauvegarder.");
      return;
    }

    if (draftPoints.length < 3) {
      setMessage("Il faut au moins 3 points pour former un polygone.");
      return;
    }

    setSaving(true);
    setMessage("");

    try {
      const center = computeCentroid(draftPoints);

      await api.put(`/campsites/${selectedSite.id}/map-shape`, {
        mapPolygonJson: JSON.stringify(draftPoints),
        labelX: center.x,
        labelY: center.y,
      });

      setMessage("Polygone sauvegardé avec succès.");
      setDraftPoints([]);
      setEditMode(false);
      await loadMap();
    } catch (err) {
      console.error(err);
      setMessage(err.message || "Impossible de sauvegarder le polygone.");
    } finally {
      setSaving(false);
    }
  };

  // ============================================================
  // MODIFICATION 2026-04-29
  // Supprime le polygone du site sélectionné dans la base de données.
  // ============================================================
  const deleteSavedPolygon = async () => {
    if (!selectedSite) {
      setMessage("Sélectionne un site avant de réinitialiser le polygone.");
      return;
    }

    const confirmed = window.confirm(
      `Réinitialiser le polygone du site ${selectedSite.siteCode} ?\n\nCette action supprimera le contour enregistré en base de données.`
    );

    if (!confirmed) return;

    setSaving(true);
    setMessage("");

    try {
      await api.delete(`/campsites/${selectedSite.id}/map-shape`);

      setDraftPoints([]);
      setEditMode(false);
      setMessage("Polygone réinitialisé avec succès.");
      await loadMap();
    } catch (err) {
      console.error(err);
      setMessage(err.message || "Impossible de réinitialiser le polygone.");
    } finally {
      setSaving(false);
    }
  };

  const getTabTitle = () => {
    if (siteTab === "to-draw") return "Contour à dessiner";
    if (siteTab === "photos") return "Photos à ajouter";
    return "Tous les sites";
  };

  const getSiteSubLabel = (site) => {
    if (siteTab === "photos") {
      return `Photos : ${site.photoCount ?? 0}/3`;
    }

    if (siteTab === "to-draw") {
      return "Aucun contour";
    }

    return `${hasPolygon(site) ? "Contour dessiné" : "Aucun contour"} • Photos : ${site.photoCount ?? 0}/3`;
  };

  const getStatusBadgeClass = (site) => {
    if (!site.isActive) {
      return "bg-slate-200 text-slate-700";
    }

    if (siteTab === "all" && !isSiteComplete(site)) {
      return "bg-red-100 text-red-700";
    }

    return "bg-emerald-100 text-emerald-700";
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-[1700px] mx-auto px-6 py-10">
        <div className="flex flex-wrap items-start justify-between gap-4 mb-6">
          <div>
            <Link
              to={`/site-manager/campgrounds/${campgroundId}/sites`}
              className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
            >
              <ArrowLeft className="w-4 h-4" />
              Retour à la gestion des sites
            </Link>

            <div className="flex items-center gap-3 mt-3">
              <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
                <ImageIcon className="w-6 h-6" />
              </div>

              <div>
                <h1 className="text-3xl font-bold text-slate-900">
                  Carte interactive du camping
                </h1>
                <p className="text-slate-600 mt-1">
                  {mapData?.campgroundName || "Chargement du camping..."}
                </p>
              </div>
            </div>
          </div>

          <div className="flex flex-wrap gap-3">
            <button
              type="button"
              onClick={loadMap}
              className="inline-flex items-center gap-2 rounded-xl bg-white border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
            >
              <RefreshCcw className="w-4 h-4" />
              Actualiser
            </button>

            <Link
              to={`/site-manager/campgrounds/${campgroundId}/sites`}
              className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
            >
              <List className="w-4 h-4" />
              Voir la liste des sites
            </Link>
          </div>
        </div>

        {error && (
          <div className="mb-6 rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {loading ? (
          <div className="bg-white rounded-3xl shadow-sm border p-8 text-slate-600">
            Chargement de la carte...
          </div>
        ) : (
          <div className="grid gap-6 xl:grid-cols-[1fr_360px]">
            <div className="space-y-6">
              <CampgroundMapViewer
                mapData={mapData}
                selectedSiteId={selectedSiteId}
                draftPoints={draftPoints}
                editMode={editMode}
                onSiteClick={handleSiteClick}
                onElementClick={handleElementClick}
                onAddPoint={addDraftPoint}
                onUpdateDraftPoint={updateDraftPoint}
                onDeleteDraftPoint={deleteDraftPoint}
              />
            </div>

            <div className="space-y-6">
              <MapLegend />

              <div className="bg-white rounded-3xl shadow-sm border p-6">
                <div className="flex gap-2 mb-4 flex-wrap">
                  <button
                    type="button"
                    onClick={() => setSiteTab("all")}
                    className={`rounded-xl px-3 py-2 text-sm font-medium ${
                      siteTab === "all"
                        ? "bg-slate-900 text-white"
                        : "bg-slate-100 text-slate-700 hover:bg-slate-200"
                    }`}
                  >
                    Tous les sites
                  </button>

                  <button
                    type="button"
                    onClick={() => setSiteTab("to-draw")}
                    className={`rounded-xl px-3 py-2 text-sm font-medium ${
                      siteTab === "to-draw"
                        ? "bg-slate-900 text-white"
                        : "bg-slate-100 text-slate-700 hover:bg-slate-200"
                    }`}
                  >
                    Contour à dessiner
                  </button>

                  <button
                    type="button"
                    onClick={() => setSiteTab("photos")}
                    className={`rounded-xl px-3 py-2 text-sm font-medium ${
                      siteTab === "photos"
                        ? "bg-slate-900 text-white"
                        : "bg-slate-100 text-slate-700 hover:bg-slate-200"
                    }`}
                  >
                    Photos à ajouter
                  </button>
                </div>

                <h2 className="text-lg font-semibold text-slate-900 mb-4">
                  {getTabTitle()}
                </h2>

                {displayedSites.length === 0 ? (
                  <p className="text-sm text-slate-600">
                    {siteTab === "photos"
                      ? "Tous les sites ont déjà 3 photos."
                      : "Aucun site disponible dans cet onglet."}
                  </p>
                ) : (
                  <div className="space-y-2 max-h-[320px] overflow-auto pr-1">
                    {displayedSites.map((site) => (
                      <button
                        key={site.id}
                        type="button"
                        onClick={() => handleSiteClick(site)}
                        className={`w-full rounded-xl border px-4 py-3 text-left transition ${
                          selectedSiteId === site.id
                            ? "border-amber-400 bg-amber-50"
                            : "border-slate-200 bg-white hover:bg-slate-50"
                        }`}
                      >
                        <div className="flex items-center justify-between gap-3">
                          <div>
                            <p className="text-sm font-semibold text-slate-900">
                              Site {site.siteCode}
                            </p>
                            <p className="text-xs text-slate-500">
                              {getSiteSubLabel(site)}
                            </p>
                          </div>

                          <span
                            className={`inline-flex rounded-full px-2.5 py-1 text-xs font-medium ${getStatusBadgeClass(site)}`}
                          >
                            {site.isActive ? "Actif" : "Inactif"}
                          </span>
                        </div>
                      </button>
                    ))}
                  </div>
                )}
              </div>

              <div className="bg-white rounded-3xl shadow-sm border p-6">
                <h2 className="text-lg font-semibold text-slate-900 mb-4">
                  Site sélectionné
                </h2>

                {selectedSite ? (
                  <div className="space-y-4">
                    <div>
                      <p className="text-sm text-slate-500">Code</p>
                      <p className="text-base font-semibold text-slate-900">
                        {selectedSite.siteCode}
                      </p>
                    </div>

                    {siteTab !== "photos" && (
                      <>
                        <div>
                          <p className="text-sm text-slate-500">État du contour</p>
                          <p className="text-sm font-medium text-slate-800">
                            {hasPolygon(selectedSite)
                              ? "Contour déjà dessiné"
                              : "Aucun contour pour ce site"}
                          </p>
                        </div>

                        <div>
                          <p className="text-sm text-slate-500">Photos</p>
                          <p className="text-sm font-medium text-slate-800">
                            {selectedSite.photoCount ?? 0}/3
                          </p>
                        </div>

                        <div className="flex flex-col gap-3">
                          <button
                            type="button"
                            onClick={openSelectedSitePage}
                            className="w-full rounded-xl bg-white border px-4 py-3 text-sm font-medium text-slate-700 hover:bg-slate-100"
                          >
                            Ouvrir la fiche du site
                          </button>

                          {!editMode ? (
                            <>
                              <button
                                type="button"
                                onClick={startEdit}
                                className="w-full rounded-xl bg-slate-900 px-4 py-3 text-sm font-medium text-white hover:bg-slate-800"
                              >
                                {hasPolygon(selectedSite)
                                  ? "Modifier le contour"
                                  : "Commencer le dessin"}
                              </button>

                              {hasPolygon(selectedSite) && (
                                <button
                                  type="button"
                                  onClick={deleteSavedPolygon}
                                  disabled={saving}
                                  className="w-full rounded-xl border border-red-200 bg-white px-4 py-3 text-sm font-medium text-red-700 hover:bg-red-50 disabled:opacity-50"
                                >
                                  Réinitialiser le polygone
                                </button>
                              )}
                            </>
                          ) : (
                            <>
                              <button
                                type="button"
                                onClick={reloadExistingPolygon}
                                className="w-full rounded-xl bg-white border px-4 py-3 text-sm font-medium text-slate-700 hover:bg-slate-100"
                              >
                                Recharger le polygone existant
                              </button>

                              <button
                                type="button"
                                onClick={removeLastPoint}
                                className="w-full rounded-xl bg-white border px-4 py-3 text-sm font-medium text-slate-700 hover:bg-slate-100"
                              >
                                Annuler le dernier point
                              </button>

                              <button
                                type="button"
                                onClick={resetDraft}
                                className="w-full rounded-xl bg-white border px-4 py-3 text-sm font-medium text-slate-700 hover:bg-slate-100"
                              >
                                Réinitialiser le brouillon
                              </button>

                              <button
                                type="button"
                                onClick={deleteSavedPolygon}
                                disabled={saving}
                                className="w-full rounded-xl border border-red-200 bg-white px-4 py-3 text-sm font-medium text-red-700 hover:bg-red-50 disabled:opacity-50"
                              >
                                Réinitialiser le polygone en BD
                              </button>

                              <button
                                type="button"
                                onClick={savePolygon}
                                disabled={saving}
                                className="w-full rounded-xl bg-slate-900 px-4 py-3 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-50"
                              >
                                {saving ? "Sauvegarde..." : "Sauvegarder le polygone"}
                              </button>

                              <button
                                type="button"
                                onClick={cancelEdit}
                                className="w-full rounded-xl bg-white border px-4 py-3 text-sm font-medium text-slate-700 hover:bg-slate-100"
                              >
                                Quitter le mode édition
                              </button>
                            </>
                          )}
                        </div>
                      </>
                    )}

                    {siteTab === "photos" && (
                      <CampsitePhotoManager campsiteId={selectedSite.id} />
                    )}
                  </div>
                ) : (
                  <p className="text-sm text-slate-600">
                    Sélectionne un site dans la liste ou sur la carte.
                  </p>
                )}

                {message && siteTab !== "photos" && (
                  <div className="mt-4 rounded-xl bg-slate-50 border px-4 py-3 text-sm text-slate-700">
                    {message}
                  </div>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}