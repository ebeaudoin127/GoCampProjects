
// ============================================================
// Fichier : frontend/src/pages/admin/CampsitesPage.jsx
// Dernière modification : 2026-04-24
//
// Résumé :
// - Liste des sites d’un camping
// - Conserve toutes les colonnes existantes
// - Ajout de la colonne "Regroupement" entre Code et Type
// - Ajout du bouton "Voir sur la carte" à côté de "Modifier"
// - Badge "Actif" rouge si contour non fait ou photos < 3
// - Sous le badge Actif :
//      1) Contour : Fait / À faire
//      2) Photos : x/3
// - Ajout du bouton "Établir la tarification"
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import {
  ArrowLeft,
  Map,
  MapPinned,
  Plus,
  RefreshCcw,
  Search,
  Pencil,
} from "lucide-react";
import { Link, useParams } from "react-router-dom";
import api from "../../services/api";

function hasPolygon(site) {
  return Array.isArray(site?.polygon) && site.polygon.length >= 3;
}

function getPhotoCount(site) {
  return site?.photoCount ?? 0;
}

function isSiteComplete(site) {
  return hasPolygon(site) && getPhotoCount(site) >= 3;
}

export default function CampsitesPage() {
  const { campgroundId } = useParams();

  const [campground, setCampground] = useState(null);
  const [sites, setSites] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");

  const loadData = async () => {
    setLoading(true);
    setError("");

    try {
      const [campgroundData, sitesData, mapData] = await Promise.all([
        api.get(`/campgrounds/${campgroundId}`),
        api.get(`/campsites/by-campground/${campgroundId}`),
        api.get(`/campgrounds/${campgroundId}/map`),
      ]);

      const rawSites = Array.isArray(sitesData) ? sitesData : [];
      const mapSites = Array.isArray(mapData?.sites) ? mapData.sites : [];

      const mergedSites = rawSites.map((site) => {
        const mapSite = mapSites.find((m) => m.id === site.id);

        return {
          ...site,
          polygon: mapSite?.polygon || [],
          photoCount: mapSite?.photoCount ?? 0,
        };
      });

      setCampground(campgroundData || null);
      setSites(mergedSites);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger les sites du camping.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [campgroundId]);

  const filteredSites = useMemo(() => {
    const q = search.trim().toLowerCase();

    if (!q) return sites;

    return sites.filter((site) => {
      const haystack = [
        site.siteCode,
        site.pricingOptionName,
        site.siteTypeName,
        site.siteServiceTypeName,
        site.siteAmperageName,
        site.isPullThrough ? "pull through" : "",
        site.isActive ? "actif" : "inactif",
        hasPolygon(site) ? "contour fait" : "contour à faire",
        `photos ${getPhotoCount(site)}`,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      return haystack.includes(q);
    });
  }, [sites, search]);

  const getStatusClass = (site) => {
    if (!site.isActive) {
      return "bg-slate-200 text-slate-700";
    }

    if (!isSiteComplete(site)) {
      return "bg-red-100 text-red-700";
    }

    return "bg-emerald-100 text-emerald-700";
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="flex flex-wrap items-start justify-between gap-4 mb-6">
          <div>
            <Link
              to="/site-manager/campgrounds"
              className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
            >
              <ArrowLeft className="w-4 h-4" />
              Retour à la gestion des campings
            </Link>

            <div className="flex items-center gap-3 mt-3">
              <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
                <MapPinned className="w-6 h-6" />
              </div>

              <div>
                <h1 className="text-3xl font-bold text-slate-900">
                  Gestion des sites
                </h1>
                <p className="text-slate-600 mt-1">
                  {campground
                    ? `Camping : ${campground.name}`
                    : "Chargement du camping..."}
                </p>
              </div>
            </div>
          </div>

          <div className="flex flex-wrap gap-3">
            <button
              type="button"
              onClick={loadData}
              className="inline-flex items-center gap-2 rounded-xl bg-white border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
            >
              <RefreshCcw className="w-4 h-4" />
              Actualiser
            </button>

            <Link
              to={`/site-manager/campgrounds/${campgroundId}/map`}
              className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
            >
              <Map className="w-4 h-4" />
              Voir la carte
            </Link>

            <Link
              to={`/site-manager/campgrounds/${campgroundId}/sites/new`}
              className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
            >
              <Plus className="w-4 h-4" />
              Ajouter un site
            </Link>

            <Link
              to={`/site-manager/campgrounds/${campgroundId}/pricing`}
              className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
            >
              <Plus className="w-4 h-4" />
              Établir la tarification
            </Link>
          </div>
        </div>

        <div className="bg-white rounded-3xl shadow-sm border p-6 mb-6">
          <div className="relative max-w-md">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Rechercher par code, regroupement, type, service, ampérage..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full rounded-xl border pl-10 pr-4 py-3"
            />
          </div>

          {error && (
            <div className="mt-4 rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          )}
        </div>

        <div className="bg-white rounded-3xl shadow-sm border overflow-hidden">
          {loading ? (
            <div className="p-8 text-slate-600">Chargement des sites...</div>
          ) : filteredSites.length === 0 ? (
            <div className="p-8 text-slate-600">Aucun site trouvé.</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full text-sm">
                <thead className="bg-slate-100 text-slate-700">
                  <tr>
                    <th className="text-left px-4 py-3 font-semibold">Code</th>
                    <th className="text-left px-4 py-3 font-semibold">
                      Regroupement
                    </th>
                    <th className="text-left px-4 py-3 font-semibold">Type</th>
                    <th className="text-left px-4 py-3 font-semibold">Service</th>
                    <th className="text-left px-4 py-3 font-semibold">Ampérage</th>
                    <th className="text-left px-4 py-3 font-semibold">Largeur (pi)</th>
                    <th className="text-left px-4 py-3 font-semibold">Longueur (pi)</th>
                    <th className="text-left px-4 py-3 font-semibold">Accès direct</th>
                    <th className="text-left px-4 py-3 font-semibold">Actif</th>
                    <th className="text-left px-4 py-3 font-semibold">Action</th>
                  </tr>
                </thead>

                <tbody>
                  {filteredSites.map((site) => (
                    <tr key={site.id} className="border-t border-slate-200">
                      <td className="px-4 py-3 font-medium text-slate-900">
                        {site.siteCode}
                      </td>

                      <td className="px-4 py-3">
                        {site.pricingOptionName ? (
                          <span className="inline-flex rounded-full bg-blue-100 px-3 py-1 text-xs font-semibold text-blue-700">
                            {site.pricingOptionName}
                          </span>
                        ) : (
                          <span className="text-slate-400">-</span>
                        )}
                      </td>

                      <td className="px-4 py-3">{site.siteTypeName || "-"}</td>
                      <td className="px-4 py-3">{site.siteServiceTypeName || "-"}</td>
                      <td className="px-4 py-3">{site.siteAmperageName || "-"}</td>
                      <td className="px-4 py-3">{site.widthFeet ?? "-"}</td>
                      <td className="px-4 py-3">{site.lengthFeet ?? "-"}</td>
                      <td className="px-4 py-3">{site.isPullThrough ? "Oui" : "Non"}</td>

                      <td className="px-4 py-3">
                        <div className="flex flex-col gap-1">
                          <span
                            className={`inline-flex w-fit rounded-full px-3 py-1 font-medium ${getStatusClass(
                              site
                            )}`}
                          >
                            {site.isActive ? "Actif" : "Inactif"}
                          </span>

                          <span className="text-xs text-slate-600">
                            Contour : {hasPolygon(site) ? "Fait" : "À faire"}
                          </span>

                          <span className="text-xs text-slate-600">
                            Photos : {getPhotoCount(site)}/3
                          </span>
                        </div>
                      </td>

                      <td className="px-4 py-3">
                        <div className="flex flex-wrap gap-2">
                          <Link
                            to={`/site-manager/campgrounds/${campgroundId}/map?siteId=${site.id}`}
                            className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                          >
                            <Map className="w-4 h-4" />
                            Voir sur la carte
                          </Link>

                          <Link
                            to={`/site-manager/campgrounds/${campgroundId}/sites/${site.id}/edit`}
                            className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-white hover:bg-slate-800"
                          >
                            <Pencil className="w-4 h-4" />
                            Modifier
                          </Link>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}