// ============================================================
// Fichier : frontend/src/components/campsite/CampsitePricingOptionField.jsx
// Dernière modification : 2026-04-20
//
// Résumé :
// - Champ de sélection d’une valeur de tarification pour un site
// - Les valeurs sont propres au camping
// - Permet d’ajouter une nouvelle valeur dans une modal
// ============================================================

import React, { useEffect, useState } from "react";
import { Plus, X } from "lucide-react";
import api from "../../services/api";

export default function CampsitePricingOptionField({
  campgroundId,
  value,
  onChange,
}) {
  const [options, setOptions] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showModal, setShowModal] = useState(false);
  const [newName, setNewName] = useState("");
  const [savingOption, setSavingOption] = useState(false);

  useEffect(() => {
    if (campgroundId) {
      loadOptions();
    } else {
      setOptions([]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [campgroundId]);

  async function loadOptions() {
    if (!campgroundId) return;

    setLoading(true);
    try {
      const response = await api.get(`/campground-site-pricing-options/by-campground/${campgroundId}`);
      const rows = Array.isArray(response)
        ? response
        : Array.isArray(response?.data)
        ? response.data
        : [];
      setOptions(rows);
    } catch (error) {
      console.error("Erreur chargement valeurs tarification :", error);
      setOptions([]);
    } finally {
      setLoading(false);
    }
  }

  async function handleCreateOption() {
    if (!newName.trim()) {
      alert("Le nom de la valeur est obligatoire.");
      return;
    }

    setSavingOption(true);
    try {
      const created = await api.post("/campground-site-pricing-options", {
        campgroundId: Number(campgroundId),
        name: newName.trim(),
        isActive: true,
      });

      await loadOptions();

      const createdId = created?.id ?? created?.data?.id ?? null;
      if (createdId) {
        onChange(String(createdId));
      }

      setNewName("");
      setShowModal(false);
    } catch (error) {
      console.error("Erreur création valeur tarification :", error);
      alert("Impossible d’ajouter cette valeur.");
    } finally {
      setSavingOption(false);
    }
  }

  return (
    <>
      <div>
        <label className="block text-sm font-medium text-slate-700 mb-1">
          Valeur de tarification
        </label>

        <div className="flex gap-2">
          <select
            value={value}
            onChange={(e) => onChange(e.target.value)}
            className="w-full rounded-xl border px-4 py-3"
            disabled={loading}
          >
            <option value="">
              {loading ? "Chargement..." : "Choisir une valeur"}
            </option>

            {options.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </select>

          <button
            type="button"
            onClick={() => setShowModal(true)}
            className="inline-flex h-[50px] w-[50px] items-center justify-center rounded-xl border bg-white text-slate-700 hover:bg-slate-50"
            title="Ajouter une nouvelle valeur"
          >
            <Plus className="h-5 w-5" />
          </button>
        </div>

        <p className="mt-1 text-xs text-slate-500">
          Cette valeur servira plus tard à la tarification du site.
        </p>
      </div>

      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4">
          <div className="w-full max-w-md rounded-3xl bg-white shadow-2xl">
            <div className="flex items-center justify-between border-b px-6 py-4">
              <h3 className="text-lg font-semibold text-slate-900">
                Ajouter une valeur de tarification
              </h3>

              <button
                type="button"
                onClick={() => {
                  setShowModal(false);
                  setNewName("");
                }}
                className="inline-flex h-10 w-10 items-center justify-center rounded-xl border text-slate-600 hover:bg-slate-50"
              >
                <X className="h-4 w-4" />
              </button>
            </div>

            <div className="px-6 py-5">
              <label className="block text-sm font-medium text-slate-700 mb-1">
                Nom de la valeur
              </label>
              <input
                type="text"
                value={newName}
                onChange={(e) => setNewName(e.target.value)}
                placeholder="Ex. Premium, Bord de l’eau, Standard A"
                className="w-full rounded-xl border px-4 py-3"
                maxLength={120}
              />
            </div>

            <div className="flex justify-end gap-3 border-t px-6 py-4">
              <button
                type="button"
                onClick={() => {
                  setShowModal(false);
                  setNewName("");
                }}
                className="rounded-xl border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
              >
                Annuler
              </button>

              <button
                type="button"
                onClick={handleCreateOption}
                disabled={savingOption}
                className="rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-60"
              >
                {savingOption ? "Ajout..." : "Ajouter"}
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
