// ============================================================
// Fichier : frontend/src/pages/admin/CampsitePricingRulesPage.jsx
// Dernière modification : 2026-04-24
//
// Résumé :
// - Page dédiée aux règles tarifaires d’un site précis
// - Affiche les règles directes du site
// - Affiche aussi les règles du regroupement tarifaire du site
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { ArrowLeft, RefreshCcw, Tag } from "lucide-react";
import { Link, useParams } from "react-router-dom";
import api from "../../services/api";

function formatCurrency(value) {
  if (value === null || value === undefined || value === "") return "-";
  return `${Number(value).toFixed(2)} $`;
}

function formatDays(days) {
  if (!Array.isArray(days) || days.length === 0) return "Tous les jours";

  const labels = {
    MONDAY: "Lundi",
    TUESDAY: "Mardi",
    WEDNESDAY: "Mercredi",
    THURSDAY: "Jeudi",
    FRIDAY: "Vendredi",
    SATURDAY: "Samedi",
    SUNDAY: "Dimanche",
  };

  return days.map((day) => labels[day] || day).join(", ");
}

function RuleTable({ title, description, rules }) {
  return (
    <div className="bg-white rounded-3xl shadow-sm border overflow-hidden">
      <div className="border-b bg-slate-50 px-6 py-5">
        <h2 className="text-xl font-semibold text-slate-900">{title}</h2>
        <p className="text-slate-600 mt-1">{description}</p>
      </div>

      {rules.length === 0 ? (
        <div className="p-8 text-slate-600">Aucune règle trouvée.</div>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full text-sm">
            <thead className="bg-slate-100 text-slate-700">
              <tr>
                <th className="text-left px-4 py-3 font-semibold">Période</th>
                <th className="text-left px-4 py-3 font-semibold">Type</th>
                <th className="text-left px-4 py-3 font-semibold">Valeurs</th>
                <th className="text-left px-4 py-3 font-semibold">Jours</th>
                <th className="text-left px-4 py-3 font-semibold">Nom</th>
                <th className="text-left px-4 py-3 font-semibold">Actif</th>
              </tr>
            </thead>

            <tbody>
              {rules.map((rule) => (
                <tr key={rule.id} className="border-t border-slate-200">
                  <td className="px-4 py-3">
                    {rule.startDate} au {rule.endDate}
                  </td>

                  <td className="px-4 py-3">
                    {rule.pricingType === "FIXED" ? "Prix fixe" : "Dynamique"}
                  </td>

                  <td className="px-4 py-3">
                    {rule.pricingType === "FIXED" ? (
                      formatCurrency(rule.fixedPrice)
                    ) : (
                      <>
                        Min {formatCurrency(rule.dynamicMinPrice)} / Réf{" "}
                        {formatCurrency(rule.dynamicBasePrice)} / Max{" "}
                        {formatCurrency(rule.dynamicMaxPrice)}
                      </>
                    )}
                  </td>

                  <td className="px-4 py-3">{formatDays(rule.daysOfWeek)}</td>
                  <td className="px-4 py-3">{rule.label || "-"}</td>

                  <td className="px-4 py-3">
                    <span
                      className={`inline-flex rounded-full px-3 py-1 text-xs font-medium ${
                        rule.isActive
                          ? "bg-emerald-100 text-emerald-700"
                          : "bg-slate-200 text-slate-700"
                      }`}
                    >
                      {rule.isActive ? "Oui" : "Non"}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default function CampsitePricingRulesPage() {
  const { campgroundId, siteId } = useParams();

  const [campground, setCampground] = useState(null);
  const [site, setSite] = useState(null);
  const [rules, setRules] = useState([]);

  const [loading, setLoading] = useState(true);
  const [loadingRules, setLoadingRules] = useState(false);
  const [error, setError] = useState("");

  const loadPage = async () => {
    setLoading(true);
    setError("");

    try {
      const [campgroundData, siteData, rulesData] = await Promise.all([
        api.get(`/campgrounds/${campgroundId}`),
        api.get(`/campsites/${siteId}`),
        api.get(`/campsite-pricing-rules/by-campground/${campgroundId}`),
      ]);

      setCampground(campgroundData || null);
      setSite(siteData || null);
      setRules(Array.isArray(rulesData) ? rulesData : []);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger les règles tarifaires du site.");
    } finally {
      setLoading(false);
    }
  };

  const refreshRules = async () => {
    setLoadingRules(true);
    setError("");

    try {
      const rulesData = await api.get(
        `/campsite-pricing-rules/by-campground/${campgroundId}`
      );
      setRules(Array.isArray(rulesData) ? rulesData : []);
    } catch (err) {
      console.error(err);
      setError("Impossible d’actualiser les règles tarifaires.");
    } finally {
      setLoadingRules(false);
    }
  };

  useEffect(() => {
    loadPage();
  }, [campgroundId, siteId]);

  const directSiteRules = useMemo(() => {
    return rules
      .filter(
        (rule) =>
          rule.targetType === "SITE" && String(rule.campsiteId) === String(siteId)
      )
      .sort((a, b) => String(a.startDate).localeCompare(String(b.startDate)));
  }, [rules, siteId]);

  const groupRules = useMemo(() => {
    if (!site?.pricingOptionId) return [];

    return rules
      .filter(
        (rule) =>
          rule.targetType === "GROUP" &&
          String(rule.pricingOptionId) === String(site.pricingOptionId)
      )
      .sort((a, b) => String(a.startDate).localeCompare(String(b.startDate)));
  }, [rules, site]);

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-600">
        Chargement des règles tarifaires du site...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="mb-6">
          <Link
            to={`/site-manager/campgrounds/${campgroundId}/sites/${siteId}/edit`}
            className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
          >
            <ArrowLeft className="w-4 h-4" />
            Retour à la modification du site
          </Link>

          <div className="flex flex-wrap items-start justify-between gap-4 mt-4">
            <div className="flex items-center gap-3">
              <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
                <Tag className="w-6 h-6" />
              </div>

              <div>
                <h1 className="text-3xl font-bold text-slate-900">
                  Règles tarifaires du site
                </h1>
                <p className="text-slate-600 mt-1">
                  {campground ? `Camping : ${campground.name}` : ""}
                </p>
                <p className="text-slate-700 mt-1 font-medium">
                  Site : {site?.siteCode || siteId}
                </p>
                <p className="text-slate-600 mt-1">
                  Regroupement : {site?.pricingOptionName || "Aucun"}
                </p>
              </div>
            </div>

            <div className="flex flex-wrap gap-3">
              <button
                type="button"
                onClick={refreshRules}
                className="inline-flex items-center gap-2 rounded-xl bg-white border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
              >
                <RefreshCcw className="w-4 h-4" />
                {loadingRules ? "Actualisation..." : "Actualiser"}
              </button>

              <Link
                to={`/site-manager/campgrounds/${campgroundId}/pricing`}
                className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
              >
                <Tag className="w-4 h-4" />
                Ouvrir le wizard de tarification
              </Link>
            </div>
          </div>
        </div>

        {error && (
          <div className="mb-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <div className="space-y-6">
          <RuleTable
            title="Règles directes du site"
            description="Règles créées spécifiquement pour ce site."
            rules={directSiteRules}
          />

          <RuleTable
            title="Règles du regroupement du site"
            description="Règles appliquées parce que ce site appartient à ce regroupement tarifaire."
            rules={groupRules}
          />
        </div>
      </div>
    </div>
  );
}