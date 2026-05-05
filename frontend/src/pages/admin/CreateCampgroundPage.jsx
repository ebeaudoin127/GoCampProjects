// ============================================================
// Fichier : src/pages/admin/CreateCampgroundPage.jsx
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Formulaire de création d’un camping
// - Charge les pays et provinces/états
// - Envoie un POST /api/campgrounds
// - Inclut les périodes de réservation configurables
// ============================================================

import React, { useEffect, useState } from "react";
import { ArrowLeft, Save } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import api from "../../services/api";
import ReservationPeriodsEditor, {
  validateReservationPeriods,
} from "../../components/campground/ReservationPeriodsEditor";

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
  reservationPeriods: [],
};

export default function CreateCampgroundPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState(initialForm);
  const [countries, setCountries] = useState([]);
  const [provinces, setProvinces] = useState([]);
  const [loadingCountries, setLoadingCountries] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  useEffect(() => {
    const loadCountries = async () => {
      setLoadingCountries(true);
      try {
        const data = await api.get("/countries");
        setCountries(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error(err);
        setError("Impossible de charger la liste des pays.");
      } finally {
        setLoadingCountries(false);
      }
    };

    loadCountries();
  }, []);

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

  const buildReservationPeriodsPayload = () => {
    return (form.reservationPeriods || []).map((period) => ({
      id: period.id || null,
      startDate: period.startDate || null,
      endDate: period.endDate || null,
      active: period.active !== false,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setSuccessMessage("");

    const reservationPeriodErrors = validateReservationPeriods(form.reservationPeriods);

    if (reservationPeriodErrors.length > 0) {
      setError(reservationPeriodErrors.join(" "));
      setSubmitting(false);
      return;
    }

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
        reservationPeriods: buildReservationPeriodsPayload(),
      };

      const created = await api.post("/campgrounds", payload);

      setSuccessMessage("Camping créé avec succès.");

      setTimeout(() => {
        navigate("/site-manager/campgrounds", { replace: true, state: { createdId: created?.id } });
      }, 800);
    } catch (err) {
      console.error(err);
      setError(err.message || "Impossible de créer le camping.");
    } finally {
      setSubmitting(false);
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
            Retour à la gestion des campings
          </Link>

          <h1 className="text-3xl font-bold text-slate-900 mt-4">
            Ajouter un camping
          </h1>
          <p className="text-slate-600 mt-2">
            Créez la fiche principale d’un nouveau camping.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Informations générales
            </h2>

            <div className="grid gap-4 md:grid-cols-2">
              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Nom du camping *
                </label>
                <input
                  type="text"
                  value={form.name}
                  onChange={(e) => updateField("name", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                />
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Description courte
                </label>
                <input
                  type="text"
                  value={form.shortDescription}
                  onChange={(e) => updateField("shortDescription", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Description complète
                </label>
                <textarea
                  value={form.longDescription}
                  onChange={(e) => updateField("longDescription", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3 min-h-[140px]"
                />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Adresse et localisation
            </h2>

            <div className="grid gap-4 md:grid-cols-2">
              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Adresse principale *
                </label>
                <input
                  type="text"
                  value={form.addressLine1}
                  onChange={(e) => updateField("addressLine1", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                />
              </div>

              <div className="md:col-span-2">
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Adresse complémentaire
                </label>
                <input
                  type="text"
                  value={form.addressLine2}
                  onChange={(e) => updateField("addressLine2", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Ville *
                </label>
                <input
                  type="text"
                  value={form.city}
                  onChange={(e) => updateField("city", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Code postal
                </label>
                <input
                  type="text"
                  value={form.postalCode}
                  onChange={(e) => updateField("postalCode", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Pays *
                </label>
                <select
                  value={form.countryId}
                  onChange={(e) => handleCountryChange(e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                  required
                  disabled={loadingCountries}
                >
                  <option value="">Choisir un pays</option>
                  {countries.map((country) => (
                    <option key={country.id} value={country.id}>
                      {country.name}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Province / État *
                </label>
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
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Latitude GPS
                </label>
                <input
                  type="number"
                  step="0.0000001"
                  value={form.gpsLatitude}
                  onChange={(e) => updateField("gpsLatitude", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Longitude GPS
                </label>
                <input
                  type="number"
                  step="0.0000001"
                  value={form.gpsLongitude}
                  onChange={(e) => updateField("gpsLongitude", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Contact
            </h2>

            <div className="grid gap-4 md:grid-cols-2">
              <TextField label="Téléphone principal" value={form.phoneMain} onChange={(value) => updateField("phoneMain", value)} />
              <TextField label="Téléphone secondaire" value={form.phoneSecondary} onChange={(value) => updateField("phoneSecondary", value)} />
              <TextField label="Courriel" type="email" value={form.email} onChange={(value) => updateField("email", value)} />
              <TextField label="Site web" value={form.website} onChange={(value) => updateField("website", value)} />
            </div>
          </div>

          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Saison et heures
            </h2>

            <div className="grid gap-4 md:grid-cols-2">
              <TextField label="Date d’ouverture" type="date" value={form.openingDate} onChange={(value) => updateField("openingDate", value)} />
              <TextField label="Date de fermeture" type="date" value={form.closingDate} onChange={(value) => updateField("closingDate", value)} />
              <TextField label="Heure d’arrivée" type="time" value={form.checkInTime} onChange={(value) => updateField("checkInTime", value)} />
              <TextField label="Heure de départ" type="time" value={form.checkOutTime} onChange={(value) => updateField("checkOutTime", value)} />
            </div>
          </div>

          <ReservationPeriodsEditor
            value={form.reservationPeriods}
            onChange={(value) => updateField("reservationPeriods", value)}
          />

          <div className="bg-white rounded-3xl shadow-sm border p-6">
            <h2 className="text-xl font-semibold text-slate-900 mb-4">
              Capacités et options
            </h2>

            <div className="grid gap-4 md:grid-cols-3">
              <NumberField label="Total des sites" value={form.totalSites} onChange={(value) => updateField("totalSites", value)} />
              <NumberField label="Sites 3 services" value={form.sites3Services} onChange={(value) => updateField("sites3Services", value)} />
              <NumberField label="Sites 2 services" value={form.sites2Services} onChange={(value) => updateField("sites2Services", value)} />
              <NumberField label="Sites 1 service" value={form.sites1Service} onChange={(value) => updateField("sites1Service", value)} />
              <NumberField label="Sites sans service" value={form.sitesNoService} onChange={(value) => updateField("sitesNoService", value)} />
              <NumberField label="Sites voyageurs" value={form.travelerSitesCount} onChange={(value) => updateField("travelerSitesCount", value)} />

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  % d’ombrage
                </label>
                <input
                  type="number"
                  min="0"
                  max="100"
                  value={form.shadePercentage}
                  onChange={(e) => updateField("shadePercentage", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>
            </div>

            <div className="grid gap-4 md:grid-cols-3 mt-6">
              <CheckboxField label="Wi-Fi disponible" checked={form.hasWifi} onChange={(value) => updateField("hasWifi", value)} />
              <CheckboxField label="Camping hivernal" checked={form.isWinterCamping} onChange={(value) => updateField("isWinterCamping", value)} />
              <CheckboxField label="Camping actif" checked={form.isActive} onChange={(value) => updateField("isActive", value)} />
            </div>
          </div>

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
              {submitting ? "Enregistrement..." : "Créer le camping"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

function TextField({ label, type = "text", value, onChange }) {
  return (
    <div>
      <label className="block text-sm font-medium text-slate-700 mb-1">
        {label}
      </label>
      <input
        type={type}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-xl border px-4 py-3"
      />
    </div>
  );
}

function NumberField({ label, value, onChange }) {
  return (
    <div>
      <label className="block text-sm font-medium text-slate-700 mb-1">
        {label}
      </label>
      <input
        type="number"
        min="0"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-xl border px-4 py-3"
      />
    </div>
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