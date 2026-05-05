// ============================================================
// Fichier : frontend/src/pages/admin/CampgroundPricingWizardPage.jsx
// Dernière modification : 2026-04-29
//
// Résumé :
// - Wizard complet restauré
// - Ajout minimumNights
// - Filtre regroupement
// - Support calendrier Excel
// - Correction du tri naturel des codes de site : 1,2,10 / A101,A102
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import {
  ArrowLeft,
  ChevronLeft,
  ChevronRight,
  Layers3,
  ListTree,
  Tag,
  Save,
  RefreshCcw,
  Pencil,
  Trash2,
  X,
  AlertTriangle,
} from "lucide-react";
import { Link, useParams } from "react-router-dom";
import api from "../../services/api";
import PricingAmountFields from "../../components/pricing/PricingAmountFields";
import PricingDaysSelector from "../../components/pricing/PricingDaysSelector";
import PricingRulesCalendar from "../../components/pricing/PricingRulesCalendar";

const PRICING_MODE = {
  SITE: "SITE",
  GROUP: "GROUP",
};

const PRICING_TYPE = {
  FIXED: "FIXED",
  DYNAMIC: "DYNAMIC",
};

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

function buildGrouping(sites) {
  const groupsMap = new Map();
  const ungrouped = [];

  sortSitesByCode(sites).forEach((site) => {
    const hasGroup = !!site.pricingOptionId || !!site.pricingOptionName;

    if (!hasGroup) {
      ungrouped.push(site);
      return;
    }

    const key = site.pricingOptionId
      ? String(site.pricingOptionId)
      : `name:${site.pricingOptionName}`;

    const groupName =
      site.pricingOptionName || `Regroupement #${site.pricingOptionId}`;

    if (!groupsMap.has(key)) {
      groupsMap.set(key, {
        key,
        pricingOptionId: site.pricingOptionId ?? null,
        pricingOptionName: groupName,
        sites: [],
      });
    }

    groupsMap.get(key).sites.push(site);
  });

  return {
    groups: Array.from(groupsMap.values()).sort((a, b) =>
      a.pricingOptionName.localeCompare(b.pricingOptionName, "fr-CA", {
        numeric: true,
        sensitivity: "base",
      })
    ),
    ungrouped: sortSitesByCode(ungrouped),
  };
}

function formatCurrency(value) {
  if (value === null || value === undefined || value === "") return "-";
  return `${Number(value).toFixed(2)} $`;
}

function formatDays(days) {
  if (!Array.isArray(days) || days.length === 0) return "Tous";

  const labels = {
    MONDAY: "Lun",
    TUESDAY: "Mar",
    WEDNESDAY: "Mer",
    THURSDAY: "Jeu",
    FRIDAY: "Ven",
    SATURDAY: "Sam",
    SUNDAY: "Dim",
  };

  return days.map((d) => labels[d] || d).join(", ");
}

function emptyRuleForm() {
  return {
    pricingType: PRICING_TYPE.FIXED,
    startDate: "",
    endDate: "",
    fixedPrice: "",
    dynamicMinPrice: "",
    dynamicBasePrice: "",
    dynamicMaxPrice: "",
    minimumNights: "",
    label: "",
    notes: "",
    isActive: true,
    daysOfWeek: [],
    selectedSiteIds: [],
    selectedGroupIds: [],
  };
}

function getRuleTargetLabel(rule) {
  return rule.targetType === "SITE"
    ? rule.siteCode || "-"
    : rule.pricingOptionName || "-";
}

export default function CampgroundPricingWizardPage() {
  const { campgroundId } = useParams();

  const [campground, setCampground] = useState(null);
  const [sites, setSites] = useState([]);
  const [pricingRules, setPricingRules] = useState([]);

  const [loading, setLoading] = useState(true);
  const [loadingRules, setLoadingRules] = useState(false);

  const [step, setStep] = useState(1);
  const [pricingMode, setPricingMode] = useState("");
  const [ruleForm, setRuleForm] = useState(emptyRuleForm());
  const [editingRuleId, setEditingRuleId] = useState(null);

  const [selectedGroupFilter, setSelectedGroupFilter] = useState("ALL");

  const [error, setError] = useState("");
  const [ruleError, setRuleError] = useState("");
  const [ruleSuccess, setRuleSuccess] = useState("");

  const [overlapModalOpen, setOverlapModalOpen] = useState(false);
  const [overlapPreview, setOverlapPreview] = useState(null);

  useEffect(() => {
    const load = async () => {
      try {
        const [c, s, r] = await Promise.all([
          api.get(`/campgrounds/${campgroundId}`),
          api.get(`/campsites/by-campground/${campgroundId}`),
          api.get(`/campsite-pricing-rules/by-campground/${campgroundId}`),
        ]);

        setCampground(c);
        setSites(sortSitesByCode(s || []));
        setPricingRules(r || []);
      } catch (e) {
        setError("Erreur chargement tarification");
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [campgroundId]);

  const sortedSites = useMemo(() => sortSitesByCode(sites), [sites]);
  const { groups } = useMemo(() => buildGrouping(sortedSites), [sortedSites]);

  const groupRules = useMemo(
    () => pricingRules.filter((r) => r.targetType === "GROUP"),
    [pricingRules]
  );

  const filteredGroupRules = useMemo(() => {
    if (selectedGroupFilter === "ALL") return groupRules;

    return groupRules.filter(
      (r) => String(r.pricingOptionId) === String(selectedGroupFilter)
    );
  }, [groupRules, selectedGroupFilter]);

  const updateRuleField = (name, value) => {
    setRuleForm((prev) => ({ ...prev, [name]: value }));
  };

  const validateRuleForm = () => {
    if (!ruleForm.startDate || !ruleForm.endDate) {
      return "Les dates de début et de fin sont obligatoires.";
    }

    if (ruleForm.pricingType === PRICING_TYPE.FIXED && ruleForm.fixedPrice === "") {
      return "Le prix fixe est obligatoire.";
    }

    if (ruleForm.pricingType === PRICING_TYPE.DYNAMIC) {
      if (
        ruleForm.dynamicMinPrice === "" ||
        ruleForm.dynamicBasePrice === "" ||
        ruleForm.dynamicMaxPrice === ""
      ) {
        return "Les prix minimum, référence et maximum sont obligatoires.";
      }

      const min = Number(ruleForm.dynamicMinPrice);
      const base = Number(ruleForm.dynamicBasePrice);
      const max = Number(ruleForm.dynamicMaxPrice);

      if (base < min) return "Le tarif de référence doit être supérieur ou égal au tarif minimum.";
      if (base > max) return "Le tarif de référence doit être inférieur ou égal au tarif maximum.";
      if (max < min) return "Le tarif maximum doit être supérieur ou égal au tarif minimum.";
    }

    if (ruleForm.minimumNights !== "") {
      const minNights = Number(ruleForm.minimumNights);

      if (!Number.isInteger(minNights) || minNights < 1) {
        return "Le minimum de nuits doit être un nombre entier supérieur ou égal à 1.";
      }
    }

    return "";
  };

  const canGoNextFromStep1 = !!pricingMode;

  const canGoNextFromStep2 =
    pricingMode === PRICING_MODE.SITE
      ? ruleForm.selectedSiteIds.length > 0
      : ruleForm.selectedGroupIds.length > 0;

  const buildCommonPayload = (forceAdjustOverlaps = false) => ({
    pricingType: ruleForm.pricingType,
    startDate: ruleForm.startDate,
    endDate: ruleForm.endDate,
    fixedPrice:
      ruleForm.pricingType === PRICING_TYPE.FIXED
        ? Number(ruleForm.fixedPrice)
        : null,
    dynamicMinPrice:
      ruleForm.pricingType === PRICING_TYPE.DYNAMIC
        ? Number(ruleForm.dynamicMinPrice)
        : null,
    dynamicBasePrice:
      ruleForm.pricingType === PRICING_TYPE.DYNAMIC
        ? Number(ruleForm.dynamicBasePrice)
        : null,
    dynamicMaxPrice:
      ruleForm.pricingType === PRICING_TYPE.DYNAMIC
        ? Number(ruleForm.dynamicMaxPrice)
        : null,
    minimumNights:
      ruleForm.minimumNights === "" ? null : Number(ruleForm.minimumNights),
    label: ruleForm.label || null,
    notes: ruleForm.notes || null,
    isActive: !!ruleForm.isActive,
    forceAdjustOverlaps,
    daysOfWeek: ruleForm.daysOfWeek,
  });

  const buildCreatePayloads = (forceAdjustOverlaps = false) => {
    const common = buildCommonPayload(forceAdjustOverlaps);

    if (pricingMode === PRICING_MODE.SITE) {
      return ruleForm.selectedSiteIds.map((siteId) => ({
        campgroundId: Number(campgroundId),
        targetType: "SITE",
        campsiteId: Number(siteId),
        pricingOptionId: null,
        ...common,
      }));
    }

    return ruleForm.selectedGroupIds.map((groupId) => ({
      campgroundId: Number(campgroundId),
      targetType: "GROUP",
      campsiteId: null,
      pricingOptionId: Number(groupId),
      ...common,
    }));
  };

  const buildUpdatePayload = (forceAdjustOverlaps = false) =>
    buildCommonPayload(forceAdjustOverlaps);

  const loadPricingRules = async () => {
    setLoadingRules(true);

    try {
      const data = await api.get(
        `/campsite-pricing-rules/by-campground/${campgroundId}`
      );
      setPricingRules(Array.isArray(data) ? data : []);
    } finally {
      setLoadingRules(false);
    }
  };

  const handleSaveRule = async () => {
    setRuleError("");
    setRuleSuccess("");

    const validationError = validateRuleForm();
    if (validationError) {
      setRuleError(validationError);
      return;
    }

    try {
      if (editingRuleId) {
        const payload = buildUpdatePayload(false);
        const preview = await api.post(
          `/campsite-pricing-rules/${editingRuleId}/preview`,
          payload
        );

        if (preview?.hasOverlap) {
          setOverlapPreview(preview);
          setOverlapModalOpen(true);
          return;
        }

        await api.put(`/campsite-pricing-rules/${editingRuleId}`, payload);
        setRuleSuccess("Règle modifiée avec succès.");
      } else {
        const payloads = buildCreatePayloads(false);
        const previews = await Promise.all(
          payloads.map((payload) =>
            api.post("/campsite-pricing-rules/preview", payload)
          )
        );

        const hasOverlap = previews.some((p) => p?.hasOverlap);
        const overlaps = previews.flatMap((p) =>
          Array.isArray(p?.overlaps) ? p.overlaps : []
        );

        if (hasOverlap) {
          setOverlapPreview({
            hasOverlap: true,
            message:
              "Certaines règles chevauchent des périodes existantes. Voulez-vous ajuster automatiquement les règles existantes ?",
            overlaps,
          });
          setOverlapModalOpen(true);
          return;
        }

        await Promise.all(
          payloads.map((payload) => api.post("/campsite-pricing-rules", payload))
        );

        setRuleSuccess("Règle(s) tarifaire(s) enregistrée(s).");
      }

      await loadPricingRules();
    } catch (err) {
      console.error(err);
      setRuleError(err.message || "Impossible d’enregistrer la règle.");
    }
  };

  const handleConfirmOverlapAdjust = async () => {
    try {
      if (editingRuleId) {
        await api.put(
          `/campsite-pricing-rules/${editingRuleId}`,
          buildUpdatePayload(true)
        );
      } else {
        await Promise.all(
          buildCreatePayloads(true).map((payload) =>
            api.post("/campsite-pricing-rules", payload)
          )
        );
      }

      setOverlapModalOpen(false);
      setOverlapPreview(null);
      setRuleSuccess("Règle enregistrée avec ajustement automatique.");
      await loadPricingRules();
    } catch (err) {
      console.error(err);
      setRuleError(err.message || "Impossible d’ajuster les règles existantes.");
    }
  };

  const handleEditRule = (rule) => {
    setEditingRuleId(rule.id);
    setPricingMode(rule.targetType);
    setStep(3);

    setRuleForm({
      pricingType: rule.pricingType,
      startDate: rule.startDate || "",
      endDate: rule.endDate || "",
      fixedPrice: rule.fixedPrice ?? "",
      dynamicMinPrice: rule.dynamicMinPrice ?? "",
      dynamicBasePrice: rule.dynamicBasePrice ?? "",
      dynamicMaxPrice: rule.dynamicMaxPrice ?? "",
      minimumNights: rule.minimumNights ?? "",
      label: rule.label || "",
      notes: rule.notes || "",
      isActive: rule.isActive !== false,
      daysOfWeek: Array.isArray(rule.daysOfWeek) ? rule.daysOfWeek : [],
      selectedSiteIds:
        rule.targetType === "SITE" && rule.campsiteId ? [rule.campsiteId] : [],
      selectedGroupIds:
        rule.targetType === "GROUP" && rule.pricingOptionId
          ? [rule.pricingOptionId]
          : [],
    });
  };

  const handleDeleteRule = async (rule) => {
    const ok = window.confirm(
      `Supprimer la règle "${rule.label || getRuleTargetLabel(rule)}" ?`
    );

    if (!ok) return;

    try {
      await api.delete(`/campsite-pricing-rules/${rule.id}`);
      setRuleSuccess("Règle supprimée.");
      await loadPricingRules();
    } catch (err) {
      console.error(err);
      setRuleError(err.message || "Impossible de supprimer cette règle.");
    }
  };

  const resetRuleForm = () => {
    setRuleForm(emptyRuleForm());
    setEditingRuleId(null);
    setRuleError("");
    setRuleSuccess("");
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-600">
        Chargement de l’assistant de tarification...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="mb-6">
          <Link
            to={`/site-manager/campgrounds/${campgroundId}/sites`}
            className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
          >
            <ArrowLeft className="w-4 h-4" />
            Retour à la gestion des sites
          </Link>

          <div className="flex items-center gap-3 mt-3">
            <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
              <Tag className="w-6 h-6" />
            </div>

            <div>
              <h1 className="text-3xl font-bold text-slate-900">
                Établir la tarification
              </h1>
              <p className="text-slate-600 mt-1">
                {campground ? `Camping : ${campground.name}` : ""}
              </p>
            </div>
          </div>
        </div>

        {error && (
          <div className="mb-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {step === 1 && (
          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-6">
              Quelle tarification voulez-vous établir ?
            </h2>

            <div className="grid gap-6 md:grid-cols-2">
              <button
                type="button"
                onClick={() => setPricingMode(PRICING_MODE.SITE)}
                className={`text-left rounded-3xl border p-6 ${
                  pricingMode === PRICING_MODE.SITE
                    ? "border-blue-500 bg-blue-50 ring-2 ring-blue-200"
                    : "hover:bg-slate-50"
                }`}
              >
                <ListTree className="w-6 h-6 mb-4" />
                <h3 className="text-lg font-semibold">Site par site</h3>
              </button>

              <button
                type="button"
                onClick={() => setPricingMode(PRICING_MODE.GROUP)}
                className={`text-left rounded-3xl border p-6 ${
                  pricingMode === PRICING_MODE.GROUP
                    ? "border-blue-500 bg-blue-50 ring-2 ring-blue-200"
                    : "hover:bg-slate-50"
                }`}
              >
                <Layers3 className="w-6 h-6 mb-4" />
                <h3 className="text-lg font-semibold">Par regroupement</h3>
              </button>
            </div>
          </div>
        )}

        {step === 2 && pricingMode === PRICING_MODE.SITE && (
          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Sélection des sites
            </h2>

            <div className="grid gap-3 md:grid-cols-4">
              {sortedSites.map((site) => (
                <label
                  key={site.id}
                  className="rounded-xl border px-4 py-3 cursor-pointer hover:bg-slate-50"
                >
                  <input
                    type="checkbox"
                    checked={ruleForm.selectedSiteIds.includes(site.id)}
                    onChange={() =>
                      updateRuleField(
                        "selectedSiteIds",
                        ruleForm.selectedSiteIds.includes(site.id)
                          ? ruleForm.selectedSiteIds.filter((id) => id !== site.id)
                          : [...ruleForm.selectedSiteIds, site.id]
                      )
                    }
                    className="mr-2"
                    disabled={!!editingRuleId}
                  />
                  {site.siteCode}
                </label>
              ))}
            </div>
          </div>
        )}

        {step === 2 && pricingMode === PRICING_MODE.GROUP && (
          <div className="space-y-4">
            {groups.map((group) => (
              <label
                key={group.key}
                className="block bg-white rounded-3xl shadow-sm border p-6 cursor-pointer hover:bg-slate-50"
              >
                <input
                  type="checkbox"
                  checked={ruleForm.selectedGroupIds.includes(group.pricingOptionId)}
                  onChange={() =>
                    updateRuleField(
                      "selectedGroupIds",
                      ruleForm.selectedGroupIds.includes(group.pricingOptionId)
                        ? ruleForm.selectedGroupIds.filter(
                            (id) => id !== group.pricingOptionId
                          )
                        : [...ruleForm.selectedGroupIds, group.pricingOptionId]
                    )
                  }
                  className="mr-3"
                  disabled={!!editingRuleId}
                />

                <span className="font-semibold text-slate-900">
                  {group.pricingOptionName}
                </span>

                <div className="mt-3 flex flex-wrap gap-2">
                  {sortSitesByCode(group.sites).map((site) => (
                    <span
                      key={site.id}
                      className="rounded-full bg-slate-100 px-3 py-1 text-sm"
                    >
                      {site.siteCode}
                    </span>
                  ))}
                </div>
              </label>
            ))}
          </div>
        )}

        {step === 3 && (
          <div className="space-y-6">
            <div className="bg-white rounded-3xl shadow-sm border p-6">
              <h2 className="text-xl font-semibold text-slate-900 mb-6">
                {editingRuleId
                  ? "Modifier une règle tarifaire"
                  : "Créer une règle tarifaire"}
              </h2>

              <div className="grid gap-6 md:grid-cols-2">
                <Field label="Date début *">
                  <input
                    type="date"
                    value={ruleForm.startDate}
                    onChange={(e) => updateRuleField("startDate", e.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>

                <Field label="Date fin *">
                  <input
                    type="date"
                    value={ruleForm.endDate}
                    onChange={(e) => updateRuleField("endDate", e.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>
              </div>

              <div className="mt-6 grid gap-4 md:grid-cols-2">
                <button
                  type="button"
                  onClick={() => updateRuleField("pricingType", PRICING_TYPE.FIXED)}
                  className={`rounded-2xl border p-4 text-left ${
                    ruleForm.pricingType === PRICING_TYPE.FIXED
                      ? "border-blue-500 bg-blue-50 ring-2 ring-blue-200"
                      : "hover:bg-slate-50"
                  }`}
                >
                  Prix fixe
                </button>

                <button
                  type="button"
                  onClick={() => updateRuleField("pricingType", PRICING_TYPE.DYNAMIC)}
                  className={`rounded-2xl border p-4 text-left ${
                    ruleForm.pricingType === PRICING_TYPE.DYNAMIC
                      ? "border-blue-500 bg-blue-50 ring-2 ring-blue-200"
                      : "hover:bg-slate-50"
                  }`}
                >
                  Tarification dynamique
                </button>
              </div>

              <div className="mt-6 max-w-md">
                <Field label="Nombre minimum de nuits">
                  <input
                    type="number"
                    min="1"
                    step="1"
                    value={ruleForm.minimumNights}
                    onChange={(e) =>
                      updateRuleField("minimumNights", e.target.value)
                    }
                    placeholder="Ex. 3 pour un long week-end"
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>
                <p className="mt-2 text-xs text-slate-500">
                  Laisse vide si aucune durée minimale n’est obligatoire.
                </p>
              </div>
            </div>

            <PricingAmountFields form={ruleForm} updateField={updateRuleField} />

            <PricingDaysSelector
              selectedDays={ruleForm.daysOfWeek}
              onChange={(days) => updateRuleField("daysOfWeek", days)}
            />

            <div className="bg-white rounded-3xl shadow-sm border p-6">
              <div className="grid gap-6 md:grid-cols-2">
                <Field label="Nom de la règle">
                  <input
                    type="text"
                    value={ruleForm.label}
                    onChange={(e) => updateRuleField("label", e.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>

                <label className="inline-flex items-center gap-3 rounded-xl border px-4 py-3 cursor-pointer h-fit self-end">
                  <input
                    type="checkbox"
                    checked={!!ruleForm.isActive}
                    onChange={(e) => updateRuleField("isActive", e.target.checked)}
                  />
                  <span className="text-sm font-medium text-slate-700">
                    Règle active
                  </span>
                </label>
              </div>

              <div className="mt-6">
                <Field label="Notes">
                  <textarea
                    value={ruleForm.notes}
                    onChange={(e) => updateRuleField("notes", e.target.value)}
                    rows={4}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>
              </div>

              {ruleError && (
                <div className="mt-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
                  {ruleError}
                </div>
              )}

              {ruleSuccess && (
                <div className="mt-6 rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">
                  {ruleSuccess}
                </div>
              )}

              <div className="mt-6 flex gap-3">
                <button
                  type="button"
                  onClick={handleSaveRule}
                  className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white hover:bg-slate-800"
                >
                  <Save className="w-4 h-4" />
                  Enregistrer
                </button>

                {editingRuleId && (
                  <button
                    type="button"
                    onClick={resetRuleForm}
                    className="rounded-xl border px-5 py-3 text-sm font-medium hover:bg-slate-50"
                  >
                    Annuler
                  </button>
                )}
              </div>
            </div>
          </div>
        )}

        <div className="mt-6 flex justify-between">
          <button
            type="button"
            onClick={() => setStep((prev) => Math.max(1, prev - 1))}
            disabled={step === 1}
            className="inline-flex items-center gap-2 rounded-xl border bg-white px-4 py-2 text-sm font-medium disabled:opacity-50"
          >
            <ChevronLeft className="w-4 h-4" />
            Précédent
          </button>

          <button
            type="button"
            onClick={() => setStep((prev) => Math.min(3, prev + 1))}
            disabled={
              step === 3 ||
              (step === 1 && !canGoNextFromStep1) ||
              (step === 2 && !canGoNextFromStep2)
            }
            className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white disabled:opacity-50"
          >
            Suivant
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>

        <div className="mt-10 space-y-6">
          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <div className="flex flex-wrap items-end justify-between gap-4">
              <div>
                <h2 className="text-xl font-semibold text-slate-900">
                  Règles tarifaires existantes
                </h2>
                <p className="text-sm text-slate-600 mt-1">
                  Filtre les règles et le calendrier par regroupement tarifaire.
                </p>
              </div>

              <div className="flex flex-wrap items-end gap-3">
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">
                    Cible
                  </label>
                  <select
                    value={selectedGroupFilter}
                    onChange={(e) => setSelectedGroupFilter(e.target.value)}
                    className="min-w-[260px] rounded-xl border px-4 py-3"
                  >
                    <option value="ALL">Tous les regroupements</option>
                    {groups.map((group) => (
                      <option key={group.key} value={group.pricingOptionId}>
                        {group.pricingOptionName}
                      </option>
                    ))}
                  </select>
                </div>

                <button
                  type="button"
                  onClick={loadPricingRules}
                  className="inline-flex items-center gap-2 rounded-xl border bg-white px-4 py-3 text-sm font-medium"
                >
                  <RefreshCcw className="w-4 h-4" />
                  {loadingRules ? "Actualisation..." : "Actualiser"}
                </button>
              </div>
            </div>
          </div>

          <PricingRulesCalendar
            rules={filteredGroupRules}
            groups={groups}
            selectedGroupFilter={selectedGroupFilter}
          />

          <div className="bg-white rounded-3xl shadow-sm border overflow-hidden">
            {filteredGroupRules.length === 0 ? (
              <div className="p-8 text-slate-600">
                Aucune règle tarifaire de regroupement pour cette cible.
              </div>
            ) : (
              <div className="overflow-x-auto">
                <table className="min-w-full text-sm">
                  <thead className="bg-slate-100 text-slate-700">
                    <tr>
                      <th className="px-4 py-3 text-left">Cible</th>
                      <th className="px-4 py-3 text-left">Période</th>
                      <th className="px-4 py-3 text-left">Type</th>
                      <th className="px-4 py-3 text-left">Valeurs</th>
                      <th className="px-4 py-3 text-left">Min. nuits</th>
                      <th className="px-4 py-3 text-left">Jours</th>
                      <th className="px-4 py-3 text-left">Actions</th>
                    </tr>
                  </thead>

                  <tbody>
                    {filteredGroupRules.map((rule) => (
                      <tr key={rule.id} className="border-t">
                        <td className="px-4 py-3 font-medium">
                          {getRuleTargetLabel(rule)}
                        </td>

                        <td className="px-4 py-3">
                          {rule.startDate} au {rule.endDate}
                        </td>

                        <td className="px-4 py-3">
                          {rule.pricingType === "FIXED" ? "Fixe" : "Dynamique"}
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

                        <td className="px-4 py-3">
                          {rule.minimumNights
                            ? `${rule.minimumNights} nuit${rule.minimumNights > 1 ? "s" : ""}`
                            : "-"}
                        </td>

                        <td className="px-4 py-3">
                          {formatDays(rule.daysOfWeek)}
                        </td>

                        <td className="px-4 py-3">
                          <div className="flex gap-2">
                            <button
                              type="button"
                              onClick={() => handleEditRule(rule)}
                              className="inline-flex items-center gap-2 rounded-xl border px-3 py-2"
                            >
                              <Pencil className="w-4 h-4" />
                              Modifier
                            </button>

                            <button
                              type="button"
                              onClick={() => handleDeleteRule(rule)}
                              className="inline-flex items-center gap-2 rounded-xl border border-red-200 px-3 py-2 text-red-700"
                            >
                              <Trash2 className="w-4 h-4" />
                              Supprimer
                            </button>
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

        {overlapModalOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4">
            <div className="w-full max-w-3xl rounded-3xl bg-white shadow-2xl">
              <div className="flex items-start justify-between border-b px-6 py-5">
                <div className="flex gap-3">
                  <AlertTriangle className="w-6 h-6 text-amber-600" />
                  <div>
                    <h3 className="text-xl font-semibold">
                      Chevauchement détecté
                    </h3>
                    <p className="text-sm text-slate-600">
                      {overlapPreview?.message}
                    </p>
                  </div>
                </div>

                <button
                  type="button"
                  onClick={() => setOverlapModalOpen(false)}
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              <div className="px-6 py-5">
                {overlapPreview?.overlaps?.length ? (
                  <div className="space-y-3">
                    {overlapPreview.overlaps.map((item, index) => (
                      <div
                        key={`${item.id}-${index}`}
                        className="rounded-xl border p-4"
                      >
                        <div className="font-medium">
                          {item.targetLabel || "-"}
                        </div>
                        <div className="text-sm text-slate-600">
                          {item.startDate} au {item.endDate}
                        </div>
                      </div>
                    ))}
                  </div>
                ) : (
                  <p className="text-sm text-slate-600">
                    Aucun détail disponible.
                  </p>
                )}
              </div>

              <div className="flex justify-end gap-3 border-t px-6 py-4">
                <button
                  type="button"
                  onClick={() => setOverlapModalOpen(false)}
                  className="rounded-xl border px-4 py-2"
                >
                  Annuler
                </button>

                <button
                  type="button"
                  onClick={handleConfirmOverlapAdjust}
                  className="rounded-xl bg-slate-900 px-4 py-2 text-white"
                >
                  Ajuster automatiquement
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

function Field({ label, children }) {
  return (
    <div>
      <label className="block text-sm font-medium text-slate-700 mb-1">
        {label}
      </label>
      {children}
    </div>
  );
}