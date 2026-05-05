// ============================================================
// Fichier : src/pages/admin/PricingPromotionsPage.jsx
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Écran admin de gestion des promotions dynamiques
// - Permet de configurer des promotions sans encore les appliquer au pricing
// - Supporte tout le camping, regroupement, site unique et multi-sites
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { ArrowLeft, Edit, Plus, Save, Trash2, X } from "lucide-react";
import { Link, useParams } from "react-router-dom";
import api from "../../services/api";

const emptyForm = {
  name: "",
  description: "",
  targetType: "ALL_CAMPGROUND",
  applicationMode: "ADJUSTMENT",
  promotionType: "PERCENT_DISCOUNT",
  campsiteId: "",
  pricingOptionId: "",
  campsiteIds: [],
  startDate: "",
  endDate: "",
  fixedPrice: "",
  discountPercent: "",
  discountAmount: "",
  buyNights: "",
  payNights: "",
  packageNights: "",
  packagePrice: "",
  requiredConsecutiveWeekends: "",
  minNights: "",
  maxNights: "",
  priority: 100,
  combinable: false,
  isActive: true,
  daysOfWeek: [],
};

const promotionTypes = [
  { value: "PERCENT_DISCOUNT", label: "Rabais en %" },
  { value: "AMOUNT_DISCOUNT", label: "Rabais en $" },
  { value: "FIXED_PRICE", label: "Prix fixe" },
  { value: "BUY_X_PAY_Y", label: "X nuits pour le prix de Y" },
  { value: "X_NIGHTS_FOR_AMOUNT", label: "X nuits pour X montant" },
  { value: "CONSECUTIVE_WEEKENDS", label: "Fins de semaine consécutives" },
  { value: "PACKAGE", label: "Forfait" },
  { value: "OTHER", label: "Autre" },
];

const targetTypes = [
  { value: "ALL_CAMPGROUND", label: "Tout le camping" },
  { value: "GROUP", label: "Regroupement tarifaire" },
  { value: "SITE", label: "Un site précis" },
  { value: "MULTI_CAMPSITE", label: "Plusieurs sites précis" },
];

const days = [
  { value: "MONDAY", label: "Lundi" },
  { value: "TUESDAY", label: "Mardi" },
  { value: "WEDNESDAY", label: "Mercredi" },
  { value: "THURSDAY", label: "Jeudi" },
  { value: "FRIDAY", label: "Vendredi" },
  { value: "SATURDAY", label: "Samedi" },
  { value: "SUNDAY", label: "Dimanche" },
];

export default function PricingPromotionsPage() {
  const { campgroundId } = useParams();

  const [campground, setCampground] = useState(null);
  const [promotions, setPromotions] = useState([]);
  const [campsites, setCampsites] = useState([]);
  const [pricingOptions, setPricingOptions] = useState([]);

  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const sortedPromotions = useMemo(() => {
    return [...promotions].sort((a, b) => {
      const priorityA = a.priority ?? 100;
      const priorityB = b.priority ?? 100;

      if (priorityA !== priorityB) return priorityA - priorityB;

      const dateA = a.startDate || "9999-12-31";
      const dateB = b.startDate || "9999-12-31";

      return dateA.localeCompare(dateB);
    });
  }, [promotions]);

  useEffect(() => {
    loadPage();
  }, [campgroundId]);

  const loadPage = async () => {
    setLoading(true);
    setError("");

    try {
      const [campgroundData, promotionsData, campsitesData, pricingOptionsData] =
        await Promise.all([
          api.get(`/campgrounds/${campgroundId}`),
          api.get(`/campgrounds/${campgroundId}/pricing-promotions`),
          api.get(`/campgrounds/${campgroundId}/sites`),
          api.get(`/campgrounds/${campgroundId}/pricing-options`),
        ]);

      setCampground(campgroundData || null);
      setPromotions(Array.isArray(promotionsData) ? promotionsData : []);
      setCampsites(Array.isArray(campsitesData) ? campsitesData : []);
      setPricingOptions(Array.isArray(pricingOptionsData) ? pricingOptionsData : []);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger les promotions dynamiques.");
    } finally {
      setLoading(false);
    }
  };

  const updateField = (name, value) => {
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
    setError("");
    setSuccessMessage("");
  };

  const toggleDay = (day) => {
    setForm((prev) => ({
      ...prev,
      daysOfWeek: prev.daysOfWeek.includes(day)
        ? prev.daysOfWeek.filter((x) => x !== day)
        : [...prev.daysOfWeek, day],
    }));
  };

  const toggleCampsite = (siteId) => {
    setForm((prev) => ({
      ...prev,
      campsiteIds: prev.campsiteIds.includes(siteId)
        ? prev.campsiteIds.filter((x) => x !== siteId)
        : [...prev.campsiteIds, siteId],
    }));
  };

  const editPromotion = (promotion) => {
    setEditingId(promotion.id);

    setForm({
      name: promotion.name || "",
      description: promotion.description || "",
      targetType: promotion.targetType || "ALL_CAMPGROUND",
      applicationMode: promotion.applicationMode || "ADJUSTMENT",
      promotionType: promotion.promotionType || "PERCENT_DISCOUNT",
      campsiteId: promotion.campsiteId ? String(promotion.campsiteId) : "",
      pricingOptionId: promotion.pricingOptionId ? String(promotion.pricingOptionId) : "",
      campsiteIds: Array.isArray(promotion.campsiteIds) ? promotion.campsiteIds : [],
      startDate: promotion.startDate || "",
      endDate: promotion.endDate || "",
      fixedPrice: promotion.fixedPrice ?? "",
      discountPercent: promotion.discountPercent ?? "",
      discountAmount: promotion.discountAmount ?? "",
      buyNights: promotion.buyNights ?? "",
      payNights: promotion.payNights ?? "",
      packageNights: promotion.packageNights ?? "",
      packagePrice: promotion.packagePrice ?? "",
      requiredConsecutiveWeekends: promotion.requiredConsecutiveWeekends ?? "",
      minNights: promotion.minNights ?? "",
      maxNights: promotion.maxNights ?? "",
      priority: promotion.priority ?? 100,
      combinable: !!promotion.combinable,
      isActive: promotion.isActive !== false,
      daysOfWeek: Array.isArray(promotion.daysOfWeek) ? promotion.daysOfWeek : [],
    });

    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const validateForm = () => {
    if (!form.name.trim()) return "Le nom de la promotion est obligatoire.";
    if (!form.startDate) return "La date de début est obligatoire.";
    if (!form.endDate) return "La date de fin est obligatoire.";
    if (form.endDate < form.startDate) return "La date de fin doit être après ou égale à la date de début.";

    if (form.targetType === "GROUP" && !form.pricingOptionId) {
      return "Sélectionne un regroupement tarifaire.";
    }

    if (form.targetType === "SITE" && !form.campsiteId) {
      return "Sélectionne un site.";
    }

    if (form.targetType === "MULTI_CAMPSITE" && form.campsiteIds.length === 0) {
      return "Sélectionne au moins un site.";
    }

    if (form.promotionType === "PERCENT_DISCOUNT") {
      if (form.discountPercent === "") return "Indique le pourcentage de rabais.";
      if (Number(form.discountPercent) <= 0 || Number(form.discountPercent) > 100) {
        return "Le pourcentage doit être entre 0 et 100.";
      }
    }

    if (form.promotionType === "AMOUNT_DISCOUNT") {
      if (form.discountAmount === "") return "Indique le montant du rabais.";
      if (Number(form.discountAmount) <= 0) return "Le montant doit être supérieur à 0.";
    }

    if (form.promotionType === "FIXED_PRICE") {
      if (form.fixedPrice === "") return "Indique le prix fixe.";
      if (Number(form.fixedPrice) <= 0) return "Le prix fixe doit être supérieur à 0.";
    }

    if (form.promotionType === "BUY_X_PAY_Y") {
      if (form.buyNights === "" || form.payNights === "") return "Indique les nuits achetées et payées.";
      if (Number(form.buyNights) <= Number(form.payNights)) {
        return "Les nuits achetées doivent être plus grandes que les nuits payées.";
      }
    }

    if (form.promotionType === "X_NIGHTS_FOR_AMOUNT") {
      if (form.packageNights === "" || form.packagePrice === "") {
        return "Indique le nombre de nuits et le montant du forfait.";
      }
    }

    if (form.promotionType === "CONSECUTIVE_WEEKENDS") {
      if (form.requiredConsecutiveWeekends === "" || form.packagePrice === "") {
        return "Indique le nombre de fins de semaine consécutives et le montant.";
      }
    }

    return "";
  };

  const numberOrNull = (value) => {
    if (value === "" || value === null || value === undefined) return null;
    return Number(value);
  };

  const buildPayload = () => ({
    name: form.name.trim(),
    description: form.description.trim() || null,
    targetType: form.targetType,
    applicationMode: form.applicationMode || "ADJUSTMENT",
    promotionType: form.promotionType,
    campsiteId: form.targetType === "SITE" ? numberOrNull(form.campsiteId) : null,
    pricingOptionId: form.targetType === "GROUP" ? numberOrNull(form.pricingOptionId) : null,
    campsiteIds: form.targetType === "MULTI_CAMPSITE" ? form.campsiteIds : [],
    startDate: form.startDate,
    endDate: form.endDate,
    fixedPrice: form.promotionType === "FIXED_PRICE" ? numberOrNull(form.fixedPrice) : null,
    discountPercent: form.promotionType === "PERCENT_DISCOUNT" ? numberOrNull(form.discountPercent) : null,
    discountAmount: form.promotionType === "AMOUNT_DISCOUNT" ? numberOrNull(form.discountAmount) : null,
    buyNights: form.promotionType === "BUY_X_PAY_Y" ? numberOrNull(form.buyNights) : null,
    payNights: form.promotionType === "BUY_X_PAY_Y" ? numberOrNull(form.payNights) : null,
    packageNights: form.promotionType === "X_NIGHTS_FOR_AMOUNT" ? numberOrNull(form.packageNights) : null,
    packagePrice:
      form.promotionType === "X_NIGHTS_FOR_AMOUNT" ||
      form.promotionType === "CONSECUTIVE_WEEKENDS" ||
      form.promotionType === "PACKAGE"
        ? numberOrNull(form.packagePrice)
        : null,
    requiredConsecutiveWeekends:
      form.promotionType === "CONSECUTIVE_WEEKENDS"
        ? numberOrNull(form.requiredConsecutiveWeekends)
        : null,
    minNights: numberOrNull(form.minNights),
    maxNights: numberOrNull(form.maxNights),
    priority: numberOrNull(form.priority) ?? 100,
    combinable: !!form.combinable,
    isActive: !!form.isActive,
    daysOfWeek: form.daysOfWeek,
  });

  const handleSubmit = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError("");
    setSuccessMessage("");

    const validationError = validateForm();

    if (validationError) {
      setError(validationError);
      setSaving(false);
      return;
    }

    try {
      const payload = buildPayload();

      if (editingId) {
        await api.put(`/campgrounds/${campgroundId}/pricing-promotions/${editingId}`, {
          data: payload,
        });
        setSuccessMessage("Promotion dynamique modifiée avec succès.");
      } else {
        await api.post(`/campgrounds/${campgroundId}/pricing-promotions`, payload);
        setSuccessMessage("Promotion dynamique créée avec succès.");
      }

      resetForm();
      await loadPage();
    } catch (err) {
      console.error(err);
      setError(err.message || "Impossible d’enregistrer la promotion.");
    } finally {
      setSaving(false);
    }
  };

  const deletePromotion = async (promotionId) => {
    const confirmed = window.confirm("Supprimer cette promotion dynamique?");

    if (!confirmed) return;

    setError("");
    setSuccessMessage("");

    try {
      await api.delete(`/campgrounds/${campgroundId}/pricing-promotions/${promotionId}`);
      setSuccessMessage("Promotion supprimée.");
      await loadPage();
    } catch (err) {
      console.error(err);
      setError(err.message || "Impossible de supprimer la promotion.");
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-600">
        Chargement des promotions dynamiques...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="mb-6 flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
          <div>
            <Link
              to={`/site-manager/campgrounds/${campgroundId}/edit`}
              className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
            >
              <ArrowLeft className="w-4 h-4" />
              Retour au camping
            </Link>

            <h1 className="text-3xl font-bold text-slate-900 mt-4">
              Promotions dynamiques
            </h1>

            <p className="text-slate-600 mt-2">
              {campground?.name
                ? `Configuration des promotions pour ${campground.name}.`
                : "Configuration des promotions du camping."}
            </p>
          </div>
        </div>

        <div className="mb-6 rounded-2xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
          Ces promotions sont configurées ici, mais ne sont pas encore appliquées automatiquement au calcul final du prix.
        </div>

        {error && (
          <div className="mb-6 rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {successMessage && (
          <div className="mb-6 rounded-xl bg-emerald-50 border border-emerald-200 px-4 py-3 text-sm text-emerald-700">
            {successMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} className="bg-white rounded-3xl shadow-sm border p-6 mb-8">
          <div className="flex items-start justify-between gap-4 mb-6">
            <div>
              <h2 className="text-xl font-semibold text-slate-900">
                {editingId ? "Modifier une promotion dynamique" : "Ajouter une promotion dynamique"}
              </h2>
              <p className="text-sm text-slate-600 mt-1">
                Choisis le type, la cible et les conditions de la promotion.
              </p>
            </div>

            {editingId && (
              <button
                type="button"
                onClick={resetForm}
                className="inline-flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
              >
                <X className="w-4 h-4" />
                Annuler
              </button>
            )}
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <Field label="Nom de la promotion *" className="md:col-span-2">
              <input
                type="text"
                value={form.name}
                onChange={(event) => updateField("name", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
                placeholder="Ex. Spécial basse saison"
                required
              />
            </Field>

            <Field label="Description" className="md:col-span-2">
              <textarea
                value={form.description}
                onChange={(event) => updateField("description", event.target.value)}
                className="w-full rounded-xl border px-4 py-3 min-h-[90px]"
                placeholder="Ex. Promotion valide sur les séjours de 3 nuits et plus."
              />
            </Field>

            <Field label="Type de promotion">
              <select
                value={form.promotionType}
                onChange={(event) => updateField("promotionType", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              >
                {promotionTypes.map((type) => (
                  <option key={type.value} value={type.value}>
                    {type.label}
                  </option>
                ))}
              </select>
            </Field>

            <Field label="Cible">
              <select
                value={form.targetType}
                onChange={(event) => updateField("targetType", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              >
                {targetTypes.map((type) => (
                  <option key={type.value} value={type.value}>
                    {type.label}
                  </option>
                ))}
              </select>
            </Field>

            {form.targetType === "GROUP" && (
              <Field label="Regroupement tarifaire" className="md:col-span-2">
                <select
                  value={form.pricingOptionId}
                  onChange={(event) => updateField("pricingOptionId", event.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                >
                  <option value="">Choisir un regroupement</option>
                  {pricingOptions.map((option) => (
                    <option key={option.id} value={option.id}>
                      {option.name || option.label || `Regroupement #${option.id}`}
                    </option>
                  ))}
                </select>
              </Field>
            )}

            {form.targetType === "SITE" && (
              <Field label="Site ciblé" className="md:col-span-2">
                <select
                  value={form.campsiteId}
                  onChange={(event) => updateField("campsiteId", event.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                >
                  <option value="">Choisir un site</option>
                  {campsites.map((site) => (
                    <option key={site.id} value={site.id}>
                      {site.code || site.name || `Site #${site.id}`}
                    </option>
                  ))}
                </select>
              </Field>
            )}

            {form.targetType === "MULTI_CAMPSITE" && (
              <div className="md:col-span-2 rounded-2xl border bg-slate-50 p-4">
                <p className="font-medium text-slate-800 mb-3">Sites ciblés</p>
                <div className="grid gap-2 md:grid-cols-4">
                  {campsites.map((site) => (
                    <label key={site.id} className="flex items-center gap-2 text-sm">
                      <input
                        type="checkbox"
                        checked={form.campsiteIds.includes(site.id)}
                        onChange={() => toggleCampsite(site.id)}
                      />
                      {site.code || site.name || `Site #${site.id}`}
                    </label>
                  ))}
                </div>
              </div>
            )}

            <Field label="Date début *">
              <input
                type="date"
                value={form.startDate}
                onChange={(event) => updateField("startDate", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              />
            </Field>

            <Field label="Date fin *">
              <input
                type="date"
                value={form.endDate}
                onChange={(event) => updateField("endDate", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              />
            </Field>

            {form.promotionType === "PERCENT_DISCOUNT" && (
              <Field label="Rabais (%)">
                <input
                  type="number"
                  min="0"
                  max="100"
                  step="0.01"
                  value={form.discountPercent}
                  onChange={(event) => updateField("discountPercent", event.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>
            )}

            {form.promotionType === "AMOUNT_DISCOUNT" && (
              <Field label="Rabais ($)">
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={form.discountAmount}
                  onChange={(event) => updateField("discountAmount", event.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>
            )}

            {form.promotionType === "FIXED_PRICE" && (
              <Field label="Prix fixe ($)">
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  value={form.fixedPrice}
                  onChange={(event) => updateField("fixedPrice", event.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>
            )}

            {form.promotionType === "BUY_X_PAY_Y" && (
              <>
                <Field label="Nuits achetées">
                  <input
                    type="number"
                    min="1"
                    value={form.buyNights}
                    onChange={(event) => updateField("buyNights", event.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>

                <Field label="Nuits payées">
                  <input
                    type="number"
                    min="1"
                    value={form.payNights}
                    onChange={(event) => updateField("payNights", event.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>
              </>
            )}

            {form.promotionType === "X_NIGHTS_FOR_AMOUNT" && (
              <>
                <Field label="Nombre de nuits">
                  <input
                    type="number"
                    min="1"
                    value={form.packageNights}
                    onChange={(event) => updateField("packageNights", event.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>

                <Field label="Montant du forfait ($)">
                  <input
                    type="number"
                    min="0"
                    step="0.01"
                    value={form.packagePrice}
                    onChange={(event) => updateField("packagePrice", event.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>
              </>
            )}

            {form.promotionType === "CONSECUTIVE_WEEKENDS" && (
              <>
                <Field label="Nombre de fins de semaine consécutives">
                  <input
                    type="number"
                    min="1"
                    value={form.requiredConsecutiveWeekends}
                    onChange={(event) => updateField("requiredConsecutiveWeekends", event.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>

                <Field label="Montant total ($)">
                  <input
                    type="number"
                    min="0"
                    step="0.01"
                    value={form.packagePrice}
                    onChange={(event) => updateField("packagePrice", event.target.value)}
                    className="w-full rounded-xl border px-4 py-3"
                  />
                </Field>
              </>
            )}

            <Field label="Minimum de nuits">
              <input
                type="number"
                min="0"
                value={form.minNights}
                onChange={(event) => updateField("minNights", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              />
            </Field>

            <Field label="Maximum de nuits">
              <input
                type="number"
                min="0"
                value={form.maxNights}
                onChange={(event) => updateField("maxNights", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              />
            </Field>

            <Field label="Priorité">
              <input
                type="number"
                value={form.priority}
                onChange={(event) => updateField("priority", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              />
            </Field>

            <div className="flex items-end gap-3">
              <CheckboxField
                label="Active"
                checked={form.isActive}
                onChange={(value) => updateField("isActive", value)}
              />
              <CheckboxField
                label="Cumulable"
                checked={form.combinable}
                onChange={(value) => updateField("combinable", value)}
              />
            </div>

            <div className="md:col-span-2 rounded-2xl border bg-slate-50 p-4">
              <p className="font-medium text-slate-800 mb-3">Jours applicables</p>
              <div className="grid gap-2 md:grid-cols-4">
                {days.map((day) => (
                  <label key={day.value} className="flex items-center gap-2 text-sm">
                    <input
                      type="checkbox"
                      checked={form.daysOfWeek.includes(day.value)}
                      onChange={() => toggleDay(day.value)}
                    />
                    {day.label}
                  </label>
                ))}
              </div>
              <p className="text-xs text-slate-500 mt-2">
                Aucun jour sélectionné = applicable tous les jours.
              </p>
            </div>
          </div>

          <div className="flex flex-wrap gap-3 mt-6">
            <button
              type="submit"
              disabled={saving}
              className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-50"
            >
              {editingId ? <Save className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
              {saving ? "Enregistrement..." : editingId ? "Sauvegarder" : "Créer la promotion"}
            </button>

            <button
              type="button"
              onClick={resetForm}
              className="rounded-xl bg-white border px-5 py-3 text-sm font-medium text-slate-700 hover:bg-slate-100"
            >
              Réinitialiser
            </button>
          </div>
        </form>

        <div className="bg-white rounded-3xl shadow-sm border p-6">
          <h2 className="text-xl font-semibold text-slate-900 mb-4">
            Promotions configurées
          </h2>

          {sortedPromotions.length === 0 ? (
            <div className="rounded-2xl border border-dashed bg-slate-50 px-4 py-6 text-sm text-slate-600">
              Aucune promotion dynamique configurée.
            </div>
          ) : (
            <div className="space-y-3">
              {sortedPromotions.map((promotion) => (
                <div key={promotion.id} className="rounded-2xl border p-4">
                  <div className="flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
                    <div>
                      <div className="flex flex-wrap items-center gap-2">
                        <h3 className="font-semibold text-slate-900">
                          {promotion.name}
                        </h3>
                        <Badge>{promotion.promotionType}</Badge>
                        <Badge>{promotion.targetType}</Badge>
                        <Badge>{promotion.isActive ? "Active" : "Inactive"}</Badge>
                      </div>

                      {promotion.description && (
                        <p className="text-sm text-slate-600 mt-1">
                          {promotion.description}
                        </p>
                      )}

                      <p className="text-sm text-slate-500 mt-2">
                        {promotion.startDate} au {promotion.endDate} · Priorité {promotion.priority}
                      </p>
                    </div>

                    <div className="flex gap-2">
                      <button
                        type="button"
                        onClick={() => editPromotion(promotion)}
                        className="inline-flex items-center gap-1 rounded-lg border px-3 py-2 text-xs font-medium text-slate-700 hover:bg-slate-100"
                      >
                        <Edit className="w-3 h-3" />
                        Modifier
                      </button>

                      <button
                        type="button"
                        onClick={() => deletePromotion(promotion.id)}
                        className="inline-flex items-center gap-1 rounded-lg border border-red-200 px-3 py-2 text-xs font-medium text-red-700 hover:bg-red-50"
                      >
                        <Trash2 className="w-3 h-3" />
                        Supprimer
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function Field({ label, children, className = "" }) {
  return (
    <div className={className}>
      <label className="block text-sm font-medium text-slate-700 mb-1">
        {label}
      </label>
      {children}
    </div>
  );
}

function CheckboxField({ label, checked, onChange }) {
  return (
    <label className="inline-flex items-center gap-2 rounded-xl border px-4 py-3 cursor-pointer bg-white">
      <input
        type="checkbox"
        checked={checked}
        onChange={(event) => onChange(event.target.checked)}
        className="h-4 w-4"
      />
      <span className="text-sm font-medium text-slate-700">{label}</span>
    </label>
  );
}

function Badge({ children }) {
  return (
    <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-700">
      {children}
    </span>
  );
}