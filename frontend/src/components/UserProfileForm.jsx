// ============================================================
// Fichier : UserProfileForm.jsx
// Dernière modification : 2026-04-16
// Résumé des modifications :
// - Retrait des champs d’équipement du profil
// - Le profil contient maintenant seulement les infos personnelles
// ============================================================

import React from "react";
import { useEffect, useState } from "react";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

export default function UserProfileForm() {
  const { user } = useAuth();

  const [countries, setCountries] = useState([]);
  const [provinces, setProvinces] = useState([]);
  const [loading, setLoading] = useState(true);

  const [form, setForm] = useState({
    firstname: "",
    lastname: "",
    phone: "",
    address: "",
    city: "",
    postalCode: "",
    countryId: "",
    provinceStateId: "",
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const userRes = await api.get("/auth/me");
        const countriesRes = await api.get("/countries");

        const u = userRes;

        setForm({
          firstname: u.firstname || "",
          lastname: u.lastname || "",
          phone: u.phone || "",
          address: u.address || "",
          city: u.city || "",
          postalCode: u.postalCode || "",
          countryId: u.country?.id || "",
          provinceStateId: u.provinceState?.id || "",
        });

        setCountries(countriesRes);
      } catch (err) {
        console.error("Erreur chargement profil:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [user]);

  useEffect(() => {
    if (!form.countryId) {
      setProvinces([]);
      return;
    }

    api
      .get(`/province-states/by-country/${form.countryId}`)
      .then((res) => setProvinces(res))
      .catch(() => setProvinces([]));
  }, [form.countryId]);

  const updateField = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const saveProfile = async () => {
    try {
      await api.put("/users/me/profile", form);
      alert("Profil mis à jour !");
    } catch (err) {
      console.error("Erreur sauvegarde :", err);
      alert("Erreur lors de l’enregistrement");
    }
  };

  if (loading) return <div className="text-center py-8">Chargement...</div>;

  return (
    <div className="max-w-2xl mx-auto space-y-4 p-4 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Mon profil</h2>

      <input
        className="border p-3 w-full rounded"
        value={form.firstname}
        onChange={(e) => updateField("firstname", e.target.value)}
        placeholder="Prénom"
      />

      <input
        className="border p-3 w-full rounded"
        value={form.lastname}
        onChange={(e) => updateField("lastname", e.target.value)}
        placeholder="Nom"
      />

      <input
        className="border p-3 w-full rounded"
        value={form.phone}
        onChange={(e) => updateField("phone", e.target.value)}
        placeholder="Téléphone"
      />

      <input
        className="border p-3 w-full rounded"
        value={form.address}
        onChange={(e) => updateField("address", e.target.value)}
        placeholder="Adresse"
      />

      <input
        className="border p-3 w-full rounded"
        value={form.city}
        onChange={(e) => updateField("city", e.target.value)}
        placeholder="Ville"
      />

      <input
        className="border p-3 w-full rounded"
        value={form.postalCode}
        onChange={(e) => updateField("postalCode", e.target.value)}
        placeholder="Code postal"
      />

      <select
        className="border p-3 w-full rounded"
        value={form.countryId}
        onChange={(e) =>
          updateField("countryId", e.target.value ? Number(e.target.value) : "")
        }
      >
        <option value="">Sélectionner un pays</option>
        {countries.map((c) => (
          <option key={c.id} value={c.id}>
            {c.name}
          </option>
        ))}
      </select>

      {form.countryId && (
        <select
          className="border p-3 w-full rounded"
          value={form.provinceStateId}
          onChange={(e) =>
            updateField(
              "provinceStateId",
              e.target.value ? Number(e.target.value) : ""
            )
          }
        >
          <option value="">
            {form.countryId === 1
              ? "Province"
              : form.countryId === 2
              ? "État"
              : "Région"}
          </option>

          {provinces.map((p) => (
            <option key={p.id} value={p.id}>
              {p.name}
            </option>
          ))}
        </select>
      )}

      <button
        className="bg-blue-600 text-white px-4 py-3 rounded w-full font-semibold hover:bg-blue-700"
        onClick={saveProfile}
      >
        Sauvegarder
      </button>
    </div>
  );
}