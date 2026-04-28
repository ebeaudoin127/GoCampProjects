// ============================================================
// Fichier : frontend/src/pages/admin/EditCampsitePage.jsx
// Dernière modification : 2026-04-24
//
// Résumé :
// - Modification d’un site
// - Charge détail + références
// - Gestion des indisponibilités temporaires du site
// - Champ valeur de tarification avec bouton + et modal
// - Ajout du bouton "Aller aux règles tarifaires de ce site"
// ============================================================

import React, { useEffect, useState } from "react";
import { ArrowLeft, Save, Tag } from "lucide-react";
import { Link, useNavigate, useParams } from "react-router-dom";
import api from "../../services/api";
import CampsiteUnavailabilityManager from "../../components/campsite/CampsiteUnavailabilityManager";
import CampsitePricingOptionField from "../../components/campsite/CampsitePricingOptionField";

const initialForm = {
  siteCode: "",
  siteTypeId: "",
  siteServiceTypeId: "",
  siteAmperageId: "",
  pricingOptionId: "",
  widthFeet: "",
  lengthFeet: "",
  maxEquipmentLengthFeet: "",
  isPullThrough: false,
  isActive: true,
  notes: "",
  equipmentAllowedTypeIds: [],
  siteSurfaceTypeIds: [],
};

export default function EditCampsitePage() {
  const { campgroundId, siteId } = useParams();
  const navigate = useNavigate();

  const [campground, setCampground] = useState(null);
  const [form, setForm] = useState(initialForm);

  const [siteTypes, setSiteTypes] = useState([]);
  const [serviceTypes, setServiceTypes] = useState([]);
  const [amperages, setAmperages] = useState([]);
  const [equipmentTypes, setEquipmentTypes] = useState([]);
  const [surfaceTypes, setSurfaceTypes] = useState([]);

  const [loadingPage, setLoadingPage] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  useEffect(() => {
    const loadPage = async () => {
      setLoadingPage(true);
      setError("");

      try {
        const [
          campgroundData,
          siteData,
          siteTypesData,
          serviceTypesData,
          amperagesData,
          equipmentData,
          surfacesData,
        ] = await Promise.all([
          api.get(`/campgrounds/${campgroundId}`),
          api.get(`/campsites/${siteId}`),
          api.get("/campsite-references/site-types"),
          api.get("/campsite-references/site-service-types"),
          api.get("/campsite-references/site-amperages"),
          api.get("/campsite-references/equipment-allowed-types"),
          api.get("/campsite-references/site-surface-types"),
        ]);

        setCampground(campgroundData || null);
        setSiteTypes(Array.isArray(siteTypesData) ? siteTypesData : []);
        setServiceTypes(Array.isArray(serviceTypesData) ? serviceTypesData : []);
        setAmperages(Array.isArray(amperagesData) ? amperagesData : []);
        setEquipmentTypes(Array.isArray(equipmentData) ? equipmentData : []);
        setSurfaceTypes(Array.isArray(surfacesData) ? surfacesData : []);

        setForm({
          siteCode: siteData.siteCode || "",
          siteTypeId: siteData.siteTypeId ? String(siteData.siteTypeId) : "",
          siteServiceTypeId: siteData.siteServiceTypeId
            ? String(siteData.siteServiceTypeId)
            : "",
          siteAmperageId: siteData.siteAmperageId
            ? String(siteData.siteAmperageId)
            : "",
          pricingOptionId: siteData.pricingOptionId
            ? String(siteData.pricingOptionId)
            : "",
          widthFeet: siteData.widthFeet ?? "",
          lengthFeet: siteData.lengthFeet ?? "",
          maxEquipmentLengthFeet: siteData.maxEquipmentLengthFeet ?? "",
          isPullThrough: !!siteData.isPullThrough,
          isActive: siteData.isActive !== false,
          notes: siteData.notes || "",
          equipmentAllowedTypeIds: Array.isArray(siteData.equipmentAllowedTypeIds)
            ? siteData.equipmentAllowedTypeIds
            : [],
          siteSurfaceTypeIds: Array.isArray(siteData.siteSurfaceTypeIds)
            ? siteData.siteSurfaceTypeIds
            : [],
        });
      } catch (err) {
        console.error(err);
        setError("Impossible de charger le site à modifier.");
      } finally {
        setLoadingPage(false);
      }
    };

    loadPage();
  }, [campgroundId, siteId]);

  const updateField = (name, value) => {
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const toggleMultiSelect = (id, fieldName) => {
    setForm((prev) => {
      const current = prev[fieldName];
      const exists = current.includes(id);

      return {
        ...prev,
        [fieldName]: exists ? current.filter((x) => x !== id) : [...current, id],
      };
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setSuccessMessage("");

    try {
      const payload = {
        siteCode: form.siteCode,
        siteTypeId: form.siteTypeId ? Number(form.siteTypeId) : null,
        siteServiceTypeId: form.siteServiceTypeId
          ? Number(form.siteServiceTypeId)
          : null,
        siteAmperageId: form.siteAmperageId
          ? Number(form.siteAmperageId)
          : null,
        pricingOptionId: form.pricingOptionId
          ? Number(form.pricingOptionId)
          : null,
        widthFeet: form.widthFeet === "" ? null : Number(form.widthFeet),
        lengthFeet: form.lengthFeet === "" ? null : Number(form.lengthFeet),
        maxEquipmentLengthFeet:
          form.maxEquipmentLengthFeet === ""
            ? null
            : Number(form.maxEquipmentLengthFeet),
        isPullThrough: !!form.isPullThrough,
        isActive: !!form.isActive,
        notes: form.notes || null,
        equipmentAllowedTypeIds: form.equipmentAllowedTypeIds,
        siteSurfaceTypeIds: form.siteSurfaceTypeIds,
      };

      await api.put(`/campsites/${siteId}`, payload);

      setSuccessMessage("Site modifié avec succès.");

      setTimeout(() => {
        navigate(`/site-manager/campgrounds/${campgroundId}/sites`, {
          replace: true,
        });
      }, 800);
    } catch (err) {
      console.error(err);
      setError(err.message || "Impossible de modifier le site.");
    } finally {
      setSubmitting(false);
    }
  };

  if (loadingPage) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-600">
        Chargement du site...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-6xl mx-auto px-6 py-10">
        <div className="mb-6">
          <Link
            to={`/site-manager/campgrounds/${campgroundId}/sites`}
            className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
          >
            <ArrowLeft className="w-4 h-4" />
            Retour à la gestion des sites
          </Link>

          <h1 className="text-3xl font-bold text-slate-900 mt-4">
            Modifier un site
          </h1>
          <p className="text-slate-600 mt-2">
            {campground ? `Camping : ${campground.name}` : ""}
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <Section title="Informations principales">
            <div className="grid gap-4 md:grid-cols-2">
              <Field label="Code du site *">
                <input
                  type="text"
                  value={form.siteCode}
                  onChange={(e) => updateField("siteCode", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                />
              </Field>

              <Field label="Type de site">
                <select
                  value={form.siteTypeId}
                  onChange={(e) => updateField("siteTypeId", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                >
                  <option value="">Choisir un type</option>
                  {siteTypes.map((item) => (
                    <option key={item.id} value={item.id}>
                      {item.nameFr}
                    </option>
                  ))}
                </select>
              </Field>

              <Field label="Type de service">
                <select
                  value={form.siteServiceTypeId}
                  onChange={(e) =>
                    updateField("siteServiceTypeId", e.target.value)
                  }
                  className="w-full rounded-xl border px-4 py-3"
                >
                  <option value="">Choisir un service</option>
                  {serviceTypes.map((item) => (
                    <option key={item.id} value={item.id}>
                      {item.nameFr}
                    </option>
                  ))}
                </select>
              </Field>

              <Field label="Ampérage">
                <select
                  value={form.siteAmperageId}
                  onChange={(e) =>
                    updateField("siteAmperageId", e.target.value)
                  }
                  className="w-full rounded-xl border px-4 py-3"
                >
                  <option value="">Choisir un ampérage</option>
                  {amperages.map((item) => (
                    <option key={item.id} value={item.id}>
                      {item.nameFr}
                    </option>
                  ))}
                </select>
              </Field>

              <CampsitePricingOptionField
                campgroundId={Number(campgroundId)}
                value={form.pricingOptionId}
                onChange={(value) => updateField("pricingOptionId", value)}
              />
            </div>
          </Section>

          <Section title="Dimensions en pieds">
            <div className="grid gap-4 md:grid-cols-3">
              <NumberField
                label="Largeur (pi)"
                value={form.widthFeet}
                onChange={(value) => updateField("widthFeet", value)}
              />
              <NumberField
                label="Longueur (pi)"
                value={form.lengthFeet}
                onChange={(value) => updateField("lengthFeet", value)}
              />
              <NumberField
                label="Longueur max équipement (pi)"
                value={form.maxEquipmentLengthFeet}
                onChange={(value) => updateField("maxEquipmentLengthFeet", value)}
              />
            </div>
          </Section>

          <Section title="Options">
            <div className="grid gap-4 md:grid-cols-2">
              <CheckboxField
                label="Accès direct"
                checked={form.isPullThrough}
                onChange={(value) => updateField("isPullThrough", value)}
              />
              <CheckboxField
                label="Site actif"
                checked={form.isActive}
                onChange={(value) => updateField("isActive", value)}
              />
            </div>

            <div className="mt-4">
              <Field label="Notes">
                <textarea
                  value={form.notes}
                  onChange={(e) => updateField("notes", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3 min-h-[120px]"
                />
              </Field>
            </div>
          </Section>

          <Section title="Équipements autorisés">
            <CheckboxGrid
              items={equipmentTypes}
              selectedIds={form.equipmentAllowedTypeIds}
              onToggle={(id) => toggleMultiSelect(id, "equipmentAllowedTypeIds")}
            />
          </Section>

          <Section title="Surfaces du site">
            <CheckboxGrid
              items={surfaceTypes}
              selectedIds={form.siteSurfaceTypeIds}
              onToggle={(id) => toggleMultiSelect(id, "siteSurfaceTypeIds")}
            />
          </Section>

          {error && (
            <div className="rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          )}

          {successMessage && (
            <div className="rounded-xl bg-emerald-50 border border-emerald-200 px-4 py-3 text-sm text-emerald-700">
              {successMessage}
            </div>
          )}

          <div className="flex flex-wrap gap-3">
            <Link
              to={`/site-manager/campgrounds/${campgroundId}/sites`}
              className="rounded-xl bg-white border px-5 py-3 text-sm font-medium text-slate-700 hover:bg-slate-100"
            >
              Annuler
            </Link>

            <button
              type="submit"
              disabled={submitting}
              className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-50"
            >
              <Save className="w-4 h-4" />
              {submitting ? "Enregistrement..." : "Sauvegarder les modifications"}
            </button>

            <Link
              to={`/site-manager/campgrounds/${campgroundId}/sites/${siteId}/pricing-rules`}
              className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-5 py-3 text-sm font-medium text-white hover:bg-blue-700"
            >
              <Tag className="w-4 h-4" />
              Aller aux règles tarifaires de ce site
            </Link>
          </div>
        </form>

        <div className="mt-6">
          <CampsiteUnavailabilityManager campsiteId={Number(siteId)} />
        </div>
      </div>
    </div>
  );
}

function Section({ title, children }) {
  return (
    <div className="bg-white rounded-3xl shadow-sm border p-6">
      <h2 className="text-xl font-semibold text-slate-900 mb-4">{title}</h2>
      {children}
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

function NumberField({ label, value, onChange }) {
  return (
    <Field label={label}>
      <input
        type="number"
        min="0"
        step="0.01"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-xl border px-4 py-3"
      />
    </Field>
  );
}

function CheckboxField({ label, checked, onChange }) {
  return (
    <label className="inline-flex items-center gap-3 rounded-xl border px-4 py-3 cursor-pointer">
      <input
        type="checkbox"
        checked={checked}
        onChange={(e) => onChange(e.target.checked)}
        className="h-4 w-4"
      />
      <span className="text-sm font-medium text-slate-700">{label}</span>
    </label>
  );
}

function CheckboxGrid({ items, selectedIds, onToggle }) {
  return (
    <div className="grid gap-3 md:grid-cols-2 xl:grid-cols-3">
      {items.map((item) => (
        <label
          key={item.id}
          className="inline-flex items-center gap-3 rounded-xl border px-4 py-3 cursor-pointer hover:bg-slate-50"
        >
          <input
            type="checkbox"
            checked={selectedIds.includes(item.id)}
            onChange={() => onToggle(item.id)}
            className="h-4 w-4"
          />
          <span className="text-sm text-slate-700">{item.nameFr}</span>
        </label>
      ))}
    </div>
  );
}