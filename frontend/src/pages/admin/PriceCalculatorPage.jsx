
// ============================================================
// Fichier : frontend/src/pages/admin/PriceCalculatorPage.jsx
// Dernière modification : 2026-05-05
//
// Résumé :
// - Calculateur de prix admin
// - Sélection du site via liste déroulante
// - Charge les sites avec plusieurs endpoints possibles
// - Envoie une demande de calcul au backend
// ============================================================

import React, { useEffect, useState } from "react";
import { ArrowLeft, Calculator } from "lucide-react";
import { Link, useParams } from "react-router-dom";
import api from "../../services/api";

export default function PriceCalculatorPage() {
  const { campgroundId } = useParams();

  const [campsites, setCampsites] = useState([]);
  const [campsiteId, setCampsiteId] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const [result, setResult] = useState(null);
  const [loadingSites, setLoadingSites] = useState(true);
  const [calculating, setCalculating] = useState(false);
  const [error, setError] = useState("");

  const asArray = (data) => {
    if (Array.isArray(data)) return data;
    if (Array.isArray(data?.content)) return data.content;
    if (Array.isArray(data?.data)) return data.data;
    if (Array.isArray(data?.items)) return data.items;
    return [];
  };

  const loadSites = async () => {
    setLoadingSites(true);
    setError("");

    const urls = [
      `/campgrounds/${campgroundId}/sites`,
      `/campsites/by-campground/${campgroundId}`,
      `/campsites?campgroundId=${campgroundId}`,
    ];

    for (const url of urls) {
      try {
        const data = await api.get(url);
        const list = asArray(data);

        if (list.length > 0) {
          setCampsites(list);
          setLoadingSites(false);
          return;
        }
      } catch (err) {
        console.warn(`Endpoint non disponible : ${url}`);
      }
    }

    setCampsites([]);
    setLoadingSites(false);
  };

  useEffect(() => {
    loadSites();
  }, [campgroundId]);

  const calculate = async () => {
    setError("");
    setResult(null);

    if (!campsiteId || !startDate || !endDate) {
      setError("Sélectionne un site, une date d’arrivée et une date de départ.");
      return;
    }

    if (endDate <= startDate) {
      setError("La date de départ doit être après la date d’arrivée.");
      return;
    }

    setCalculating(true);

    try {
      const response = await api.post("/pricing-calculator", {
        campgroundId: Number(campgroundId),
        campsiteId: Number(campsiteId),
        startDate,
        endDate,
      });

      setResult(response);
    } catch (err) {
      console.error(err);
      setError(err.message || "Erreur lors du calcul du prix.");
    } finally {
      setCalculating(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-5xl mx-auto px-6 py-10">
        <div className="mb-6">
          <Link
            to="/site-manager/campgrounds"
            className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
          >
            <ArrowLeft className="w-4 h-4" />
            Retour aux campings
          </Link>

          <div className="flex items-center gap-3 mt-4">
            <div className="rounded-2xl bg-purple-100 p-3 text-purple-700">
              <Calculator className="w-6 h-6" />
            </div>

            <div>
              <h1 className="text-3xl font-bold text-slate-900">
                Calculateur de prix
              </h1>
              <p className="text-slate-600 mt-1">
                Teste le prix d’un séjour sans créer de réservation.
              </p>
            </div>
          </div>
        </div>

        <div className="grid gap-6 lg:grid-cols-[380px_minmax(0,1fr)]">
          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Paramètres du séjour
            </h2>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Site
                </label>

                <select
                  value={campsiteId}
                  onChange={(e) => setCampsiteId(e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  disabled={loadingSites}
                >
                  <option value="">
                    {loadingSites ? "Chargement des sites..." : "Choisir un site"}
                  </option>

                  {campsites.map((site) => (
                    <option key={site.id} value={site.id}>
                      {site.code || site.name || `Site #${site.id}`}
                    </option>
                  ))}
                </select>

                {!loadingSites && campsites.length === 0 && (
                  <p className="mt-2 text-sm text-amber-700">
                    Aucun site trouvé pour ce camping.
                  </p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Date d’arrivée
                </label>
                <input
                  type="date"
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Date de départ
                </label>
                <input
                  type="date"
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <button
                type="button"
                onClick={calculate}
                disabled={calculating || loadingSites}
                className="w-full rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-50"
              >
                {calculating ? "Calcul en cours..." : "Calculer le prix"}
              </button>
            </div>

            {error && (
              <div className="mt-4 rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
                {error}
              </div>
            )}
          </div>

          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Résultat du calcul
            </h2>

            {!result ? (
              <div className="rounded-2xl border border-dashed bg-slate-50 px-4 py-8 text-sm text-slate-600">
                Remplis les paramètres du séjour, puis clique sur “Calculer le prix”.
              </div>
            ) : (
              <div className="space-y-6">
                <div className="grid gap-4 md:grid-cols-2">
                  <div className="rounded-2xl bg-slate-50 p-4">
                    <p className="text-sm text-slate-500">Nombre de nuits</p>
                    <p className="text-2xl font-bold text-slate-900">
                      {result.nights}
                    </p>
                  </div>

                  <div className="rounded-2xl bg-emerald-50 p-4">
                    <p className="text-sm text-emerald-700">Total</p>
                    <p className="text-2xl font-bold text-emerald-800">
                      {Number(result.total || 0).toFixed(2)} $
                    </p>
                  </div>
                </div>

                <div>
                  <h3 className="font-semibold text-slate-900 mb-3">
                    Détail par nuit
                  </h3>

                  {Array.isArray(result.lines) && result.lines.length > 0 ? (
                    <div className="overflow-x-auto rounded-2xl border">
                      <table className="min-w-full text-sm">
                        <thead className="bg-slate-100 text-slate-700">
                          <tr>
                            <th className="text-left px-4 py-3 font-semibold">
                              Date
                            </th>
                            <th className="text-left px-4 py-3 font-semibold">
                              Règle
                            </th>
                            <th className="text-right px-4 py-3 font-semibold">
                              Montant
                            </th>
                          </tr>
                        </thead>

                        <tbody>
                          {result.lines.map((line, index) => (
                            <tr key={`${line.date}-${index}`} className="border-t">
                              <td className="px-4 py-3">{line.date}</td>
                              <td className="px-4 py-3">
                                {line.label || "Prix journalier"}
                              </td>
                              <td className="px-4 py-3 text-right font-medium">
                                {Number(line.amount || 0).toFixed(2)} $
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                  ) : (
                    <div className="rounded-2xl border border-dashed bg-slate-50 px-4 py-6 text-sm text-slate-600">
                      Aucun détail retourné par le backend.
                    </div>
                  )}
                </div>

                <div className="rounded-2xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
                  V1 du calculateur : le calcul utilise les règles tarifaires de base. Les promotions dynamiques seront branchées ensuite.
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}