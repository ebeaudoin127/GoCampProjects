// ============================================================
// Fichier : EquipementForm.jsx
// Dernière modification : 2026-04-16
// Résumé des modifications :
// - Nouveau composant pour gérer l’équipement dans l’onglet
//   "Équipement"
// - Chargement de l’équipement existant du user connecté
// - Création ou mise à jour via /users/me/equipements
// ============================================================

import React, { useEffect, useState } from "react";
import api from "../services/api";

export default function EquipementForm() {
  const [loading, setLoading] = useState(true);
  const [equipementId, setEquipementId] = useState(null);
  const [message, setMessage] = useState("");

  const [form, setForm] = useState({
    marque: "",
    modele: "",
    longueur: "",
    noSerie: "",
    noPlaque: "",
    hasExtension: false,
    extensionConducteur: "",
    extensionPassager: "",
    actif: true,
  });

  useEffect(() => {
    const fetchEquipement = async () => {
      try {
        const data = await api.get("/users/me/equipements");

        if (Array.isArray(data) && data.length > 0) {
          const e = data[0];

          setEquipementId(e.id);
          setForm({
            marque: e.marque || "",
            modele: e.modele || "",
            longueur: e.longueur || "",
            noSerie: e.noSerie || "",
            noPlaque: e.noPlaque || "",
            hasExtension: Boolean(e.hasExtension),
            extensionConducteur: e.extensionConducteur || "",
            extensionPassager: e.extensionPassager || "",
            actif: e.actif ?? true,
          });
        }
      } catch (err) {
        console.error("Erreur chargement équipement :", err);
      } finally {
        setLoading(false);
      }
    };

    fetchEquipement();
  }, []);

  const updateField = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const saveEquipement = async () => {
    setMessage("");

    const payload = {
      marque: form.marque || null,
      modele: form.modele || null,
      longueur: form.longueur ? Number(form.longueur) : null,
      noSerie: form.noSerie || null,
      noPlaque: form.noPlaque || null,
      hasExtension: Boolean(form.hasExtension),
      extensionConducteur:
        form.hasExtension && form.extensionConducteur !== ""
          ? Number(form.extensionConducteur)
          : null,
      extensionPassager:
        form.hasExtension && form.extensionPassager !== ""
          ? Number(form.extensionPassager)
          : null,
      actif: Boolean(form.actif),
    };

    try {
      if (equipementId) {
        const updated = await api.put(
          `/users/me/equipements/${equipementId}`,
          payload
        );
        setEquipementId(updated.id);
        setMessage("Équipement mis à jour avec succès.");
      } else {
        const created = await api.post("/users/me/equipements", payload);
        setEquipementId(created.id);
        setMessage("Équipement créé avec succès.");
      }
    } catch (err) {
      console.error("Erreur sauvegarde équipement :", err);
      setMessage(
        err?.response?.data || "Erreur lors de l’enregistrement de l’équipement."
      );
    }
  };

  if (loading) {
    return <div className="text-center py-8">Chargement...</div>;
  }

  return (
    <div className="max-w-xl mx-auto space-y-4 p-4 bg-white rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Mon équipement</h2>

      <input
        className="border p-2 w-full rounded"
        value={form.marque}
        onChange={(e) => updateField("marque", e.target.value)}
        placeholder="Marque"
      />

      <input
        className="border p-2 w-full rounded"
        value={form.modele}
        onChange={(e) => updateField("modele", e.target.value)}
        placeholder="Modèle"
      />

      <input
        type="number"
        className="border p-2 w-full rounded"
        value={form.longueur}
        onChange={(e) => updateField("longueur", e.target.value)}
        placeholder="Longueur de l'équipement (pieds)"
      />

      <input
        className="border p-2 w-full rounded"
        value={form.noSerie}
        onChange={(e) => updateField("noSerie", e.target.value)}
        placeholder="Numéro de série"
      />

      <input
        className="border p-2 w-full rounded"
        value={form.noPlaque}
        onChange={(e) => updateField("noPlaque", e.target.value)}
        placeholder="Numéro de plaque"
      />

      <label className="flex items-center gap-2">
        <input
          type="checkbox"
          checked={form.hasExtension}
          onChange={(e) => updateField("hasExtension", e.target.checked)}
        />
        <span>A des extensions (Slide-Out)</span>
      </label>

      {form.hasExtension && (
        <>
          <input
            type="number"
            className="border p-2 w-full rounded"
            value={form.extensionConducteur}
            onChange={(e) =>
              updateField("extensionConducteur", e.target.value)
            }
            placeholder="Extension côté conducteur (pouces)"
          />

          <input
            type="number"
            className="border p-2 w-full rounded"
            value={form.extensionPassager}
            onChange={(e) => updateField("extensionPassager", e.target.value)}
            placeholder="Extension côté passager (pouces)"
          />
        </>
      )}

      {message && <p className="text-center text-orange-600">{message}</p>}

      <button
        className="bg-blue-600 text-white px-4 py-2 rounded w-full"
        onClick={saveEquipement}
      >
        Sauvegarder
      </button>
    </div>
  );
}43ezx