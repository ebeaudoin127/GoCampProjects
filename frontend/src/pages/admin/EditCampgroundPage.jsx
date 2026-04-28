// ============================================================
// Fichier : src/pages/admin/EditCampgroundPage.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Formulaire d’édition d’un camping
// - Ajout des services / activités / hébergements
// - Intégration des icônes via IconRenderer
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { ArrowLeft, Save } from "lucide-react";
import { Link, useNavigate, useParams } from "react-router-dom";
import api from "../../services/api";
import IconRenderer from "../../components/IconRenderer";

const initialForm = {
  name: "",
  shortDescription: "",
  longDescription: "",
  addressLine1: "",
  addressLine2: "",
  city: "",
  provinceStateId: "",
  countryId: "",
  postalCode: "",
  phoneMain: "",
  phoneSecondary: "",
  email: "",
  website: "",
  gpsLatitude: "",
  gpsLongitude: "",
  openingDate: "",
  closingDate: "",
  checkInTime: "",
  checkOutTime: "",
  totalSites: 0,
  sites3Services: 0,
  sites2Services: 0,
  sites1Service: 0,
  sitesNoService: 0,
  travelerSitesCount: 0,
  shadePercentage: "",
  hasWifi: false,
  isWinterCamping: false,
  isActive: true,
};

export default function EditCampgroundPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState(initialForm);
  const [countries, setCountries] = useState([]);
  const [provinces, setProvinces] = useState([]);
  const [services, setServices] = useState([]);
  const [activities, setActivities] = useState([]);
  const [accommodationTypes, setAccommodationTypes] = useState([]);

  const [selectedServiceIds, setSelectedServiceIds] = useState([]);
  const [selectedActivityIds, setSelectedActivityIds] = useState([]);
  const [selectedAccommodationTypeIds, setSelectedAccommodationTypeIds] = useState([]);

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
          countriesData,
          campgroundData,
          servicesData,
          activitiesData,
          accommodationTypesData,
          featuresData,
        ] = await Promise.all([
          api.get("/countries"),
          api.get(`/campgrounds/${id}`),
          api.get("/campgrounds/services"),
          api.get("/campgrounds/activities"),
          api.get("/campgrounds/accommodation-types"),
          api.get(`/campgrounds/${id}/features`),
        ]);

        setCountries(Array.isArray(countriesData) ? countriesData : []);
        setServices(Array.isArray(servicesData) ? servicesData : []);
        setActivities(Array.isArray(activitiesData) ? activitiesData : []);
        setAccommodationTypes(Array.isArray(accommodationTypesData) ? accommodationTypesData : []);

        const countryId = campgroundData.countryId ? String(campgroundData.countryId) : "";
        const provinceStateId = campgroundData.provinceStateId ? String(campgroundData.provinceStateId) : "";

        if (countryId) {
          const provincesData = await api.get(`/province-states/by-country/${countryId}`);
          setProvinces(Array.isArray(provincesData) ? provincesData : []);
        }

        setForm({
          name: campgroundData.name || "",
          shortDescription: campgroundData.shortDescription || "",
          longDescription: campgroundData.longDescription || "",
          addressLine1: campgroundData.addressLine1 || "",
          addressLine2: campgroundData.addressLine2 || "",
          city: campgroundData.city || "",
          provinceStateId,
          countryId,
          postalCode: campgroundData.postalCode || "",
          phoneMain: campgroundData.phoneMain || "",
          phoneSecondary: campgroundData.phoneSecondary || "",
          email: campgroundData.email || "",
          website: campgroundData.website || "",
          gpsLatitude: campgroundData.gpsLatitude ?? "",
          gpsLongitude: campgroundData.gpsLongitude ?? "",
          openingDate: campgroundData.openingDate || "",
          closingDate: campgroundData.closingDate || "",
          checkInTime: campgroundData.checkInTime || "",
          checkOutTime: campgroundData.checkOutTime || "",
          totalSites: campgroundData.totalSites ?? 0,
          sites3Services: campgroundData.sites3Services ?? 0,
          sites2Services: campgroundData.sites2Services ?? 0,
          sites1Service: campgroundData.sites1Service ?? 0,
          sitesNoService: campgroundData.sitesNoService ?? 0,
          travelerSitesCount: campgroundData.travelerSitesCount ?? 0,
          shadePercentage: campgroundData.shadePercentage ?? "",
          hasWifi: !!campgroundData.hasWifi,
          isWinterCamping: !!campgroundData.isWinterCamping,
          isActive: campgroundData.isActive !== false,
        });

        setSelectedServiceIds(
          Array.isArray(featuresData.serviceIds) ? featuresData.serviceIds : []
        );
        setSelectedActivityIds(
          Array.isArray(featuresData.activityIds) ? featuresData.activityIds : []
        );
        setSelectedAccommodationTypeIds(
          Array.isArray(featuresData.accommodationTypeIds) ? featuresData.accommodationTypeIds : []
        );
      } catch (err) {
        console.error(err);
        setError("Impossible de charger le camping à modifier.");
      } finally {
        setLoadingPage(false);
      }
    };

    loadPage();
  }, [id]);

  const groupedAccommodationTypes = useMemo(() => {
    const groups = {};

    for (const item of accommodationTypes) {
      const key = item.category || "AUTRE";
      if (!groups[key]) groups[key] = [];
      groups[key].push(item);
    }

    return groups;
  }, [accommodationTypes]);

  const loadProvinces = async (countryId) => {
    if (!countryId) {
      setProvinces([]);
      return;
    }

    try {
      const data = await api.get(`/province-states/by-country/${countryId}`);
      setProvinces(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger les provinces/états.");
      setProvinces([]);
    }
  };

  const updateField = (name, value) => {
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleCountryChange = async (countryId) => {
    updateField("countryId", countryId);
    updateField("provinceStateId", "");
    await loadProvinces(countryId);
  };

  const toggleSelection = (itemId, setSelectedIds) => {
    setSelectedIds((prev) =>
      prev.includes(itemId) ? prev.filter((x) => x !== itemId) : [...prev, itemId]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setSuccessMessage("");

    try {
      const payload = {
        name: form.name,
        shortDescription: form.shortDescription || null,
        longDescription: form.longDescription || null,
        addressLine1: form.addressLine1,
        addressLine2: form.addressLine2 || null,
        city: form.city,
        provinceStateId: form.provinceStateId ? Number(form.provinceStateId) : null,
        countryId: form.countryId ? Number(form.countryId) : null,
        postalCode: form.postalCode || null,
        phoneMain: form.phoneMain || null,
        phoneSecondary: form.phoneSecondary || null,
        email: form.email || null,
        website: form.website || null,
        gpsLatitude: form.gpsLatitude === "" ? null : Number(form.gpsLatitude),
        gpsLongitude: form.gpsLongitude === "" ? null : Number(form.gpsLongitude),
        openingDate: form.openingDate || null,
        closingDate: form.closingDate || null,
        checkInTime: form.checkInTime || null,
        checkOutTime: form.checkOutTime || null,
        totalSites: Number(form.totalSites || 0),
        sites3Services: Number(form.sites3Services || 0),
        sites2Services: Number(form.sites2Services || 0),
        sites1Service: Number(form.sites1Service || 0),
        sitesNoService: Number(form.sitesNoService || 0),
        travelerSitesCount: Number(form.travelerSitesCount || 0),
        shadePercentage: form.shadePercentage === "" ? null : Number(form.shadePercentage),
        hasWifi: !!form.hasWifi,
        isWinterCamping: !!form.isWinterCamping,
        isActive: !!form.isActive,
      };

      await api.put(`/campgrounds/${id}`, payload);

      await api.put(`/campgrounds/${id}/features`, {
        serviceIds: selectedServiceIds,
        activityIds: selectedActivityIds,
        accommodationTypeIds: selectedAccommodationTypeIds,
      });

      setSuccessMessage("Camping modifié avec succès.");

      setTimeout(() => {
        navigate("/site-manager/campgrounds", { replace: true });
      }, 800);
    } catch (err) {
      console.error(err);
      setError(err.message || "Impossible de modifier le camping.");
    } finally {
      setSubmitting(false);
    }
  };

  if (loadingPage) {
    return (
      <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-600">
        Chargement du camping...
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-6xl mx-auto px-6 py-10">
        <div className="mb-6">
          <Link
            to="/site-manager/campgrounds"
            className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
          >
            <ArrowLeft className="w-4 h-4" />
            Retour à la gestion des campings
          </Link>

          <h1 className="text-3xl font-bold text-slate-900 mt-4">
            Modifier un camping
          </h1>
          <p className="text-slate-600 mt-2">
            Modifiez la fiche principale et les caractéristiques du camping.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <Section title="Informations générales">
            <div className="grid gap-4 md:grid-cols-2">
              <Field label="Nom du camping *">
                <input
                  type="text"
                  value={form.name}
                  onChange={(e) => updateField("name", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                />
              </Field>

              <Field label="Description courte" className="md:col-span-2">
                <input
                  type="text"
                  value={form.shortDescription}
                  onChange={(e) => updateField("shortDescription", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Description complète" className="md:col-span-2">
                <textarea
                  value={form.longDescription}
                  onChange={(e) => updateField("longDescription", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3 min-h-[140px]"
                />
              </Field>
            </div>
          </Section>

          <Section title="Adresse et localisation">
            <div className="grid gap-4 md:grid-cols-2">
              <Field label="Adresse principale *" className="md:col-span-2">
                <input
                  type="text"
                  value={form.addressLine1}
                  onChange={(e) => updateField("addressLine1", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                />
              </Field>

              <Field label="Adresse complémentaire" className="md:col-span-2">
                <input
                  type="text"
                  value={form.addressLine2}
                  onChange={(e) => updateField("addressLine2", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Ville *">
                <input
                  type="text"
                  value={form.city}
                  onChange={(e) => updateField("city", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                />
              </Field>

              <Field label="Code postal">
                <input
                  type="text"
                  value={form.postalCode}
                  onChange={(e) => updateField("postalCode", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Pays *">
                <select
                  value={form.countryId}
                  onChange={(e) => handleCountryChange(e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                >
                  <option value="">Choisir un pays</option>
                  {countries.map((country) => (
                    <option key={country.id} value={country.id}>
                      {country.name}
                    </option>
                  ))}
                </select>
              </Field>

              <Field label="Province / État *">
                <select
                  value={form.provinceStateId}
                  onChange={(e) => updateField("provinceStateId", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                >
                  <option value="">Choisir une province / un état</option>
                  {provinces.map((province) => (
                    <option key={province.id} value={province.id}>
                      {province.name}
                    </option>
                  ))}
                </select>
              </Field>

              <Field label="Latitude GPS">
                <input
                  type="number"
                  step="0.0000001"
                  value={form.gpsLatitude}
                  onChange={(e) => updateField("gpsLatitude", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Longitude GPS">
                <input
                  type="number"
                  step="0.0000001"
                  value={form.gpsLongitude}
                  onChange={(e) => updateField("gpsLongitude", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>
            </div>
          </Section>

          <Section title="Contact">
            <div className="grid gap-4 md:grid-cols-2">
              <Field label="Téléphone principal">
                <input
                  type="text"
                  value={form.phoneMain}
                  onChange={(e) => updateField("phoneMain", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Téléphone secondaire">
                <input
                  type="text"
                  value={form.phoneSecondary}
                  onChange={(e) => updateField("phoneSecondary", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Courriel">
                <input
                  type="email"
                  value={form.email}
                  onChange={(e) => updateField("email", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Site web">
                <input
                  type="text"
                  value={form.website}
                  onChange={(e) => updateField("website", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>
            </div>
          </Section>

          <Section title="Saison et heures">
            <div className="grid gap-4 md:grid-cols-2">
              <Field label="Date d’ouverture">
                <input
                  type="date"
                  value={form.openingDate}
                  onChange={(e) => updateField("openingDate", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Date de fermeture">
                <input
                  type="date"
                  value={form.closingDate}
                  onChange={(e) => updateField("closingDate", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Heure d’arrivée">
                <input
                  type="time"
                  value={form.checkInTime}
                  onChange={(e) => updateField("checkInTime", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>

              <Field label="Heure de départ">
                <input
                  type="time"
                  value={form.checkOutTime}
                  onChange={(e) => updateField("checkOutTime", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>
            </div>
          </Section>

          <Section title="Capacités et options">
            <div className="grid gap-4 md:grid-cols-3">
              <NumberField label="Total des sites" value={form.totalSites} onChange={(value) => updateField("totalSites", value)} />
              <NumberField label="Sites 3 services" value={form.sites3Services} onChange={(value) => updateField("sites3Services", value)} />
              <NumberField label="Sites 2 services" value={form.sites2Services} onChange={(value) => updateField("sites2Services", value)} />
              <NumberField label="Sites 1 service" value={form.sites1Service} onChange={(value) => updateField("sites1Service", value)} />
              <NumberField label="Sites sans service" value={form.sitesNoService} onChange={(value) => updateField("sitesNoService", value)} />
              <NumberField label="Sites voyageurs" value={form.travelerSitesCount} onChange={(value) => updateField("travelerSitesCount", value)} />

              <Field label="% d’ombrage">
                <input
                  type="number"
                  min="0"
                  max="100"
                  value={form.shadePercentage}
                  onChange={(e) => updateField("shadePercentage", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </Field>
            </div>

            <div className="grid gap-4 md:grid-cols-3 mt-6">
              <CheckboxField label="Wi-Fi disponible" checked={form.hasWifi} onChange={(value) => updateField("hasWifi", value)} />
              <CheckboxField label="Camping hivernal" checked={form.isWinterCamping} onChange={(value) => updateField("isWinterCamping", value)} />
              <CheckboxField label="Camping actif" checked={form.isActive} onChange={(value) => updateField("isActive", value)} />
            </div>
          </Section>

          <Section title="Services">
            <CheckboxGridWithIcons
              items={services}
              selectedIds={selectedServiceIds}
              onToggle={(itemId) => toggleSelection(itemId, setSelectedServiceIds)}
            />
          </Section>

          <Section title="Activités">
            <CheckboxGridWithIcons
              items={activities}
              selectedIds={selectedActivityIds}
              onToggle={(itemId) => toggleSelection(itemId, setSelectedActivityIds)}
            />
          </Section>

          <Section title="Hébergements">
            <div className="space-y-6">
              {Object.entries(groupedAccommodationTypes).map(([category, items]) => (
                <div key={category}>
                  <h3 className="text-sm font-semibold text-slate-800 mb-3">
                    {category}
                  </h3>

                  <CheckboxGridWithIcons
                    items={items}
                    selectedIds={selectedAccommodationTypeIds}
                    onToggle={(itemId) => toggleSelection(itemId, setSelectedAccommodationTypeIds)}
                  />
                </div>
              ))}
            </div>
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
              to="/site-manager/campgrounds"
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
          </div>
        </form>
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

function Field({ label, children, className = "" }) {
  return (
    <div className={className}>
      <label className="block text-sm font-medium text-slate-700 mb-1">{label}</label>
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

function CheckboxGridWithIcons({ items, selectedIds, onToggle }) {
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

          <span className="inline-flex h-9 w-9 items-center justify-center rounded-full bg-slate-100 text-slate-700">
            <IconRenderer
              name={item.icon || item.code}
              className="w-5 h-5"
            />
          </span>

          <span className="text-sm text-slate-700">{item.nameFr}</span>
        </label>
      ))}
    </div>
  );
}