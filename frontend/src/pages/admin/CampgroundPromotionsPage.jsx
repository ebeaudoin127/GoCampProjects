// ============================================================
// Fichier : src/pages/admin/CampgroundPromotionsPage.jsx
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Page admin de gestion des promotions marketing d’un camping
// - Utilise la table campground_promotion
// - CRUD complet : liste, création, modification, suppression
// - Ne touche pas au moteur pricing_promotion
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { ArrowLeft, Edit, Plus, Save, Trash2, X } from "lucide-react";
import { Link, useParams } from "react-router-dom";
import api from "../../services/api";

const emptyForm = {
  title: "",
  description: "",
  promoCode: "",
  startDate: "",
  endDate: "",
  discountType: "PERCENT",
  discountValue: "",
  conditionsText: "",
  isActive: true,
};

const discountTypes = [
  { value: "PERCENT", label: "Pourcentage (%)" },
  { value: "AMOUNT", label: "Montant fixe ($)" },
  { value: "FIXED_PRICE", label: "Prix fixe" },
  { value: "NIGHTS_FOR_PRICE", label: "Nuits pour prix spécial" },
  { value: "OTHER", label: "Autre / informatif" },
];

export default function CampgroundPromotionsPage() {
  const { id } = useParams();

  const [campground, setCampground] = useState(null);
  const [promotions, setPromotions] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const sortedPromotions = useMemo(() => {
    return [...promotions].sort((a, b) => {
      const aDate = a.startDate || "9999-12-31";
      const bDate = b.startDate || "9999-12-31";
      return aDate.localeCompare(bDate);
    });
  }, [promotions]);

  useEffect(() => {
    loadPage();
  }, [id]);

  const loadPage = async () => {
    setLoading(true);
    setError("");

    try {
      const [campgroundData, promotionsData] = await Promise.all([
        api.get(`/campgrounds/${id}`),
        api.get(`/campgrounds/${id}/promotions`),
      ]);

      setCampground(campgroundData || null);
      setPromotions(Array.isArray(promotionsData) ? promotionsData : []);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger les promotions du camping.");
    } finally {
      setLoading(false);
    }
  };

  const updateField = (name, value) => {
    setForm((previous) => ({
      ...previous,
      [name]: value,
    }));
  };

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
    setError("");
    setSuccessMessage("");
  };

  const editPromotion = (promotion) => {
    setEditingId(promotion.id);
    setForm({
      title: promotion.title || "",
      description: promotion.description || "",
      promoCode: promotion.promoCode || "",
      startDate: promotion.startDate || "",
      endDate: promotion.endDate || "",
      discountType: promotion.discountType || "PERCENT",
      discountValue: promotion.discountValue ?? "",
      conditionsText: promotion.conditionsText || "",
      isActive: promotion.isActive !== false,
    });

    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const validateForm = () => {
    if (!form.title.trim()) {
      return "Le titre de la promotion est obligatoire.";
    }

    if (form.startDate && form.endDate && form.endDate < form.startDate) {
      return "La date de fin doit être après ou égale à la date de début.";
    }

    if (form.discountType !== "OTHER") {
      if (form.discountValue === "" || form.discountValue === null) {
        return "La valeur du rabais est obligatoire pour ce type de promotion.";
      }

      const numericValue = Number(form.discountValue);

      if (Number.isNaN(numericValue) || numericValue < 0) {
        return "La valeur du rabais doit être un nombre positif.";
      }

      if (form.discountType === "PERCENT" && numericValue > 100) {
        return "Un rabais en pourcentage ne peut pas dépasser 100%.";
      }
    }

    return "";
  };

  const buildPayload = () => ({
    title: form.title.trim(),
    description: form.description.trim() || null,
    promoCode: form.promoCode.trim() || null,
    startDate: form.startDate || null,
    endDate: form.endDate || null,
    discountType: form.discountType || "OTHER",
    discountValue:
      form.discountType === "OTHER" || form.discountValue === ""
        ? null
        : Number(form.discountValue),
    conditionsText: form.conditionsText.trim() || null,
    isActive: !!form.isActive,
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
        await api.put(`/campgrounds/${id}/promotions/${editingId}`, payload);
        setSuccessMessage("Promotion modifiée avec succès.");
      } else {
        await api.post(`/campgrounds/${id}/promotions`, payload);
        setSuccessMessage("Promotion créée avec succès.");
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
    const confirmed = window.confirm("Supprimer cette promotion?");

    if (!confirmed) {
      return;
    }

    setError("");
    setSuccessMessage("");

    try {
      await api.delete(`/campgrounds/${id}/promotions/${promotionId}`);
      setSuccessMessage("Promotion supprimée avec succès.");
      await loadPage();

      if (editingId === promotionId) {
        resetForm();
      }
    } catch (err) {
      console.error(err);
      setError(err.message || "Impossible de supprimer la promotion.");
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-600">
        Chargement des promotions...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-6xl mx-auto px-6 py-10">
        <div className="mb-6">
          <Link
            to={`/site-manager/campgrounds/${id}/edit`}
            className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
          >
            <ArrowLeft className="w-4 h-4" />
            Retour au camping
          </Link>

          <h1 className="text-3xl font-bold text-slate-900 mt-4">
            Promotions du camping
          </h1>

          <p className="text-slate-600 mt-2">
            {campground?.name
              ? `Gestion des promotions marketing pour ${campground.name}.`
              : "Gestion des promotions marketing du camping."}
          </p>
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
                {editingId ? "Modifier une promotion" : "Ajouter une promotion"}
              </h2>
              <p className="text-sm text-slate-600 mt-1">
                Ces promotions servent à l’affichage marketing du camping.
              </p>
            </div>

            {editingId && (
              <button
                type="button"
                onClick={resetForm}
                className="inline-flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
              >
                <X className="w-4 h-4" />
                Annuler modification
              </button>
            )}
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <Field label="Titre *" className="md:col-span-2">
              <input
                type="text"
                value={form.title}
                onChange={(event) => updateField("title", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
                required
              />
            </Field>

            <Field label="Description" className="md:col-span-2">
              <textarea
                value={form.description}
                onChange={(event) => updateField("description", event.target.value)}
                className="w-full rounded-xl border px-4 py-3 min-h-[100px]"
              />
            </Field>

            <Field label="Code promo">
              <input
                type="text"
                value={form.promoCode}
                onChange={(event) => updateField("promoCode", event.target.value.toUpperCase())}
                className="w-full rounded-xl border px-4 py-3"
                placeholder="ETE2026"
              />
            </Field>

            <Field label="Type de promotion">
              <select
                value={form.discountType}
                onChange={(event) => updateField("discountType", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              >
                {discountTypes.map((type) => (
                  <option key={type.value} value={type.value}>
                    {type.label}
                  </option>
                ))}
              </select>
            </Field>

            <Field label="Date début">
              <input
                type="date"
                value={form.startDate}
                onChange={(event) => updateField("startDate", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              />
            </Field>

            <Field label="Date fin">
              <input
                type="date"
                value={form.endDate}
                onChange={(event) => updateField("endDate", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
              />
            </Field>

            <Field label="Valeur">
              <input
                type="number"
                min="0"
                step="0.01"
                value={form.discountValue}
                onChange={(event) => updateField("discountValue", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
                disabled={form.discountType === "OTHER"}
              />
            </Field>

            <div className="flex items-end">
              <CheckboxField
                label="Promotion active"
                checked={form.isActive}
                onChange={(value) => updateField("isActive", value)}
              />
            </div>

            <Field label="Conditions affichées" className="md:col-span-2">
              <input
                type="text"
                maxLength={500}
                value={form.conditionsText}
                onChange={(event) => updateField("conditionsText", event.target.value)}
                className="w-full rounded-xl border px-4 py-3"
                placeholder="Ex. Valide sur les séjours de 2 nuits et plus."
              />
            </Field>
          </div>

          <div className="flex flex-wrap gap-3 mt-6">
            <button
              type="submit"
              disabled={saving}
              className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-50"
            >
              {editingId ? <Save className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
              {saving
                ? "Enregistrement..."
                : editingId
                  ? "Sauvegarder la promotion"
                  : "Créer la promotion"}
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
            Promotions existantes
          </h2>

          {sortedPromotions.length === 0 ? (
            <div className="rounded-2xl border border-dashed bg-slate-50 px-4 py-6 text-sm text-slate-600">
              Aucune promotion marketing n’est configurée pour ce camping.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b text-left text-slate-500">
                    <th className="py-3 pr-4 font-medium">Titre</th>
                    <th className="py-3 pr-4 font-medium">Code</th>
                    <th className="py-3 pr-4 font-medium">Type</th>
                    <th className="py-3 pr-4 font-medium">Valeur</th>
                    <th className="py-3 pr-4 font-medium">Dates</th>
                    <th className="py-3 pr-4 font-medium">Statut</th>
                    <th className="py-3 pr-4 font-medium text-right">Actions</th>
                  </tr>
                </thead>

                <tbody>
                  {sortedPromotions.map((promotion) => (
                    <tr key={promotion.id} className="border-b last:border-b-0">
                      <td className="py-4 pr-4 align-top">
                        <div className="font-semibold text-slate-900">
                          {promotion.title}
                        </div>
                        {promotion.description && (
                          <div className="text-slate-500 mt-1 max-w-md">
                            {promotion.description}
                          </div>
                        )}
                        {promotion.conditionsText && (
                          <div className="text-xs text-slate-500 mt-2">
                            Conditions : {promotion.conditionsText}
                          </div>
                        )}
                      </td>

                      <td className="py-4 pr-4 align-top">
                        {promotion.promoCode ? (
                          <span className="rounded-lg bg-slate-100 px-2 py-1 font-mono text-xs text-slate-800">
                            {promotion.promoCode}
                          </span>
                        ) : (
                          <span className="text-slate-400">—</span>
                        )}
                      </td>

                      <td className="py-4 pr-4 align-top">
                        {getDiscountTypeLabel(promotion.discountType)}
                      </td>

                      <td className="py-4 pr-4 align-top">
                        {formatDiscountValue(promotion)}
                      </td>

                      <td className="py-4 pr-4 align-top">
                        {promotion.startDate || promotion.endDate ? (
                          <span>
                            {promotion.startDate || "—"} au {promotion.endDate || "—"}
                          </span>
                        ) : (
                          <span className="text-slate-400">Toujours</span>
                        )}
                      </td>

                      <td className="py-4 pr-4 align-top">
                        <span
                          className={
                            promotion.isActive !== false
                              ? "rounded-full bg-emerald-50 px-3 py-1 text-xs font-medium text-emerald-700"
                              : "rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-500"
                          }
                        >
                          {promotion.isActive !== false ? "Active" : "Inactive"}
                        </span>
                      </td>

                      <td className="py-4 pr-4 align-top">
                        <div className="flex justify-end gap-2">
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
    <label className="inline-flex items-center gap-3 rounded-xl border px-4 py-3 cursor-pointer w-full">
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

function getDiscountTypeLabel(type) {
  const found = discountTypes.find((item) => item.value === type);
  return found ? found.label : type || "—";
}

function formatDiscountValue(promotion) {
  if (promotion.discountType === "OTHER") {
    return "—";
  }

  if (promotion.discountValue === null || promotion.discountValue === undefined) {
    return "—";
  }

  if (promotion.discountType === "PERCENT") {
    return `${promotion.discountValue}%`;
  }

  if (promotion.discountType === "AMOUNT") {
    return `${promotion.discountValue}$`;
  }

  if (promotion.discountType === "FIXED_PRICE") {
    return `${promotion.discountValue}$`;
  }

  return promotion.discountValue;
}