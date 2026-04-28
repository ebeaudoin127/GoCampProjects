// ============================================================
// Fichier : frontend/src/pages/admin/SiteCampgroundsPage.jsx
// Dernière modification : 2026-04-18
//
// Résumé :
// - Liste des campings
// - Ajout du bouton "Gérer les sites"
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import {
  ArrowLeft,
  Building2,
  Pencil,
  Plus,
  RefreshCcw,
  Search,
  MapPinned,
} from "lucide-react";
import { Link } from "react-router-dom";
import api from "../../services/api";

export default function SiteCampgroundsPage() {
  const [campgrounds, setCampgrounds] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [search, setSearch] = useState("");

  const loadCampgrounds = async () => {
    setLoading(true);
    setError("");

    try {
      const data = await api.get("/campgrounds");
      setCampgrounds(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger la liste des campings.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCampgrounds();
  }, []);

  const filteredCampgrounds = useMemo(() => {
    const q = search.trim().toLowerCase();

    if (!q) return campgrounds;

    return campgrounds.filter((c) => {
      const haystack = [
        c.name,
        c.city,
        c.countryName,
        c.provinceStateName,
        c.email,
        c.phoneMain,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      return haystack.includes(q);
    });
  }, [campgrounds, search]);

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="flex flex-wrap items-center justify-between gap-4 mb-6">
          <div>
            <Link
              to="/site-manager"
              className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
            >
              <ArrowLeft className="w-4 h-4" />
              Retour au gestionnaire du site
            </Link>

            <div className="flex items-center gap-3 mt-3">
              <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
                <Building2 className="w-6 h-6" />
              </div>

              <div>
                <h1 className="text-3xl font-bold text-slate-900">
                  Gestion des campings
                </h1>
                <p className="text-slate-600 mt-1">
                  Consultez et administrez les campings de la plateforme.
                </p>
              </div>
            </div>
          </div>

          <div className="flex flex-wrap gap-3">
            <button
              type="button"
              onClick={loadCampgrounds}
              className="inline-flex items-center gap-2 rounded-xl bg-white border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
            >
              <RefreshCcw className="w-4 h-4" />
              Actualiser
            </button>

            <Link
              to="/site-manager/campgrounds/new"
              className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
            >
              <Plus className="w-4 h-4" />
              Ajouter un camping
            </Link>
          </div>
        </div>

        <div className="bg-white rounded-3xl shadow-sm border p-6 mb-6">
          <div className="relative max-w-md">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Rechercher par nom, ville, province, pays..."
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
            <div className="p-8 text-slate-600">Chargement des campings...</div>
          ) : filteredCampgrounds.length === 0 ? (
            <div className="p-8 text-slate-600">Aucun camping trouvé.</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full text-sm">
                <thead className="bg-slate-100 text-slate-700">
                  <tr>
                    <th className="text-left px-4 py-3 font-semibold">ID</th>
                    <th className="text-left px-4 py-3 font-semibold">Nom</th>
                    <th className="text-left px-4 py-3 font-semibold">Ville</th>
                    <th className="text-left px-4 py-3 font-semibold">Province/État</th>
                    <th className="text-left px-4 py-3 font-semibold">Pays</th>
                    <th className="text-left px-4 py-3 font-semibold">Téléphone</th>
                    <th className="text-left px-4 py-3 font-semibold">Courriel</th>
                    <th className="text-left px-4 py-3 font-semibold">Sites</th>
                    <th className="text-left px-4 py-3 font-semibold">Wi-Fi</th>
                    <th className="text-left px-4 py-3 font-semibold">Hivernal</th>
                    <th className="text-left px-4 py-3 font-semibold">Actif</th>
                    <th className="text-left px-4 py-3 font-semibold">Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {filteredCampgrounds.map((c) => (
                    <tr key={c.id} className="border-t border-slate-200">
                      <td className="px-4 py-3">{c.id}</td>
                      <td className="px-4 py-3 font-medium text-slate-900">{c.name}</td>
                      <td className="px-4 py-3">{c.city || "-"}</td>
                      <td className="px-4 py-3">{c.provinceStateName || "-"}</td>
                      <td className="px-4 py-3">{c.countryName || "-"}</td>
                      <td className="px-4 py-3">{c.phoneMain || "-"}</td>
                      <td className="px-4 py-3">{c.email || "-"}</td>
                      <td className="px-4 py-3">{c.totalSites ?? 0}</td>
                      <td className="px-4 py-3">{c.hasWifi ? "Oui" : "Non"}</td>
                      <td className="px-4 py-3">{c.isWinterCamping ? "Oui" : "Non"}</td>
                      <td className="px-4 py-3">
                        <span
                          className={`inline-flex rounded-full px-3 py-1 font-medium ${
                            c.isActive
                              ? "bg-emerald-100 text-emerald-700"
                              : "bg-slate-200 text-slate-700"
                          }`}
                        >
                          {c.isActive ? "Actif" : "Inactif"}
                        </span>
                      </td>
                      <td className="px-4 py-3">
                        <div className="flex flex-wrap gap-2">
                          <Link
                            to={`/site-manager/campgrounds/${c.id}/sites`}
                            className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-white hover:bg-blue-700"
                          >
                            <MapPinned className="w-4 h-4" />
                            Gérer les sites
                          </Link>

                          <Link
                            to={`/site-manager/campgrounds/${c.id}/edit`}
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
