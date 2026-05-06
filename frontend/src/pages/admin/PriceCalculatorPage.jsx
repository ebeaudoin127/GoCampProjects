





// ============================================================
// Fichier : frontend/src/pages/admin/PriceCalculatorPage.jsx
// Dernière modification : 2026-05-05
// Auteur : ChatGPT
//
// Résumé :
// - Calculateur de prix admin
// - Permet de tester un séjour sans créer de réservation
// - Ajout du champ code promo
// - Affiche indisponibilité, prix de base, rabais et total final
// ============================================================

import React, { useEffect, useState } from "react";
import {
  AlertTriangle,
  ArrowLeft,
  BadgePercent,
  Calculator,
} from "lucide-react";
import { Link, useParams } from "react-router-dom";
import api from "../../services/api";

export default function PriceCalculatorPage() {
  const { campgroundId } = useParams();

  const [campsites, setCampsites] = useState([]);
  const [campsiteId, setCampsiteId] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [promoCode, setPromoCode] = useState("");

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
        promoCode: promoCode.trim() || null,
      });

      setResult(response);
    } catch (err) {
      console.error(err);
      setError(err.message || "Erreur lors du calcul du prix.");
    } finally {
      setCalculating(false);
    }
  };

  const formatMoney = (value) => {
    return `${Number(value || 0).toFixed(2)} $`;
  };

  const unavailabilities = Array.isArray(result?.unavailabilities)
    ? result.unavailabilities
    : [];

  const appliedPromotions = Array.isArray(result?.appliedPromotions)
    ? result.appliedPromotions
    : [];

  const hasOldShape = result && result.basePrice !== undefined;
  const baseTotal = result?.baseTotal ?? result?.basePrice ?? 0;
  const discountTotal = result?.promotionDiscountTotal ?? result?.discount ?? 0;
  const finalTotal = result?.total ?? result?.finalPrice ?? 0;

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
                  onChange={(event) => setCampsiteId(event.target.value)}
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
                  onChange={(event) => setStartDate(event.target.value)}
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
                  onChange={(event) => setEndDate(event.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <div className="rounded-2xl border bg-slate-50 p-4">
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Code promo
                </label>
                <input
                  type="text"
                  value={promoCode}
                  onChange={(event) => setPromoCode(event.target.value.toUpperCase())}
                  className="w-full rounded-xl border px-4 py-3 bg-white"
                  placeholder="Ex. ETE2026"
                />
                <p className="text-xs text-slate-500 mt-2">
                  Optionnel. Sert à tester les promotions qui exigent un code.
                </p>
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
                {!result.available && (
                  <div className="rounded-2xl border border-red-200 bg-red-50 p-5">
                    <div className="flex items-start gap-3">
                      <AlertTriangle className="mt-0.5 h-5 w-5 text-red-700" />
                      <div>
                        <h3 className="font-semibold text-red-900">
                          Calcul impossible
                        </h3>
                        <p className="mt-1 text-sm text-red-800">
                          {result.message ||
                            result.explanation ||
                            "Le séjour ne peut pas être calculé pour la période sélectionnée."}
                        </p>
                      </div>
                    </div>

                    {unavailabilities.length > 0 && (
                      <div className="mt-4 space-y-3">
                        <h4 className="text-sm font-semibold text-red-900">
                          Périodes d’indisponibilité détectées
                        </h4>

                        {unavailabilities.map((item) => (
                          <div
                            key={item.id}
                            className="rounded-xl border border-red-200 bg-white p-4 text-sm"
                          >
                            <p className="font-medium text-slate-900">
                              Du {item.startDate} au {item.endDate}
                            </p>

                            {item.reason && (
                              <p className="mt-1 text-slate-700">
                                <strong>Raison :</strong> {item.reason}
                              </p>
                            )}

                            {item.notes && (
                              <p className="mt-1 text-slate-700">
                                <strong>Notes :</strong> {item.notes}
                              </p>
                            )}
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                )}

                {result.available && (
                  <>
                    <div className="grid gap-4 md:grid-cols-3">
                      <div className="rounded-2xl bg-slate-50 p-4">
                        <p className="text-sm text-slate-500">Prix de base</p>
                        <p className="text-2xl font-bold text-slate-900">
                          {formatMoney(baseTotal)}
                        </p>
                      </div>

                      <div className="rounded-2xl bg-emerald-50 p-4">
                        <p className="text-sm text-emerald-700">
                          Rabais promotions
                        </p>
                        <p className="text-2xl font-bold text-emerald-800">
                          -{formatMoney(discountTotal)}
                        </p>
                      </div>

                      <div className="rounded-2xl bg-purple-50 p-4">
                        <p className="text-sm text-purple-700">Total final</p>
                        <p className="text-2xl font-bold text-purple-800">
                          {formatMoney(finalTotal)}
                        </p>
                      </div>
                    </div>

                    {result.message && (
                      <div className="rounded-2xl border bg-slate-50 p-4 text-sm text-slate-700">
                        {result.message}
                      </div>
                    )}

                    {hasOldShape && result.appliedPromotionName && (
                      <div className="rounded-2xl border border-emerald-200 bg-emerald-50 p-5">
                        <div className="flex items-center gap-2 mb-2">
                          <BadgePercent className="h-5 w-5 text-emerald-700" />
                          <h3 className="font-semibold text-emerald-900">
                            Promotion appliquée
                          </h3>
                        </div>

                        <p className="font-semibold text-slate-900">
                          {result.appliedPromotionName}
                        </p>

                        {result.explanation && (
                          <p className="text-sm text-slate-700 mt-1">
                            {result.explanation}
                          </p>
                        )}
                      </div>
                    )}

                    {appliedPromotions.length > 0 && (
                      <div className="rounded-2xl border border-emerald-200 bg-emerald-50 p-5">
                        <div className="flex items-center gap-2 mb-3">
                          <BadgePercent className="h-5 w-5 text-emerald-700" />
                          <h3 className="font-semibold text-emerald-900">
                            Promotions appliquées
                          </h3>
                        </div>

                        <div className="space-y-3">
                          {appliedPromotions.map((promo) => (
                            <div
                              key={promo.id}
                              className="rounded-xl bg-white border p-4 text-sm"
                            >
                              <div className="flex flex-wrap items-center justify-between gap-2">
                                <div>
                                  <p className="font-semibold text-slate-900">
                                    {promo.name}
                                  </p>
                                  <p className="text-slate-500">
                                    {promo.promotionType}
                                    {promo.description ? ` · ${promo.description}` : ""}
                                  </p>
                                </div>

                                <p className="font-bold text-emerald-700">
                                  -{formatMoney(promo.discountAmount)}
                                </p>
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>
                    )}

                    {Array.isArray(result.lines) && result.lines.length > 0 && (
                      <div>
                        <h3 className="font-semibold text-slate-900 mb-3">
                          Détail par nuit
                        </h3>

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
                                    {formatMoney(line.amount)}
                                  </td>
                                </tr>
                              ))}
                            </tbody>
                          </table>
                        </div>
                      </div>
                    )}

                    {!Array.isArray(result.lines) && (
                      <div className="rounded-2xl border border-dashed bg-slate-50 px-4 py-6 text-sm text-slate-600">
                        Ce calcul utilise l’ancien format de réponse du backend. Les détails par nuit seront disponibles avec la version détaillée du service.
                      </div>
                    )}
                  </>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}