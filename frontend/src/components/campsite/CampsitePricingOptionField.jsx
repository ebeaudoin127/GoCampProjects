// ============================================================
// Fichier : frontend/src/components/campsite/CampsitePricingOptionField.jsx
// Dernière modification : 2026-05-05
//
// Résumé :
// - Champ de sélection d’une valeur de tarification pour un site
// - Les valeurs sont propres au camping
// - Permet d’ajouter une nouvelle valeur dans une modal
// - Permet de supprimer une valeur seulement si elle n’est pas utilisée
// - Ajoute une fenêtre d’aide détaillée
// ============================================================

import React, { useEffect, useState } from "react";
import { HelpCircle, Minus, Plus, Trash2, X } from "lucide-react";
import api from "../../services/api";
import CampsitePricingOptionHelpModal from "./CampsitePricingOptionHelpModal";

export default function CampsitePricingOptionField({
  campgroundId,
  value,
  onChange,
}) {
  const [options, setOptions] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showAddModal, setShowAddModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showHelpModal, setShowHelpModal] = useState(false);

  const [newName, setNewName] = useState("");
  const [deleteId, setDeleteId] = useState("");

  const [savingOption, setSavingOption] = useState(false);
  const [deletingOption, setDeletingOption] = useState(false);

  useEffect(() => {
    if (campgroundId) {
      loadOptions();
    } else {
      setOptions([]);
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [campgroundId]);

  const asArray = (data) => {
    if (Array.isArray(data)) return data;
    if (Array.isArray(data?.data)) return data.data;
    if (Array.isArray(data?.content)) return data.content;
    if (Array.isArray(data?.items)) return data.items;
    return [];
  };

  async function loadOptions() {
    if (!campgroundId) return;

    setLoading(true);

    try {
      const response = await api.get(
        `/campground-site-pricing-options/by-campground/${campgroundId}`
      );

      setOptions(asArray(response));
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
      setShowAddModal(false);
    } catch (error) {
      console.error("Erreur création valeur tarification :", error);
      alert(error?.message || "Impossible d’ajouter cette valeur.");
    } finally {
      setSavingOption(false);
    }
  }

  async function handleDeleteOption() {
    if (!deleteId) {
      alert("Sélectionne une valeur à supprimer.");
      return;
    }

    const selectedOption = options.find(
      (item) => String(item.id) === String(deleteId)
    );

    const confirmed = window.confirm(
      `Supprimer la valeur "${selectedOption?.name || deleteId}"?\n\nLa suppression sera refusée si elle est utilisée.`
    );

    if (!confirmed) return;

    setDeletingOption(true);

    try {
      await api.delete(`/campground-site-pricing-options/${deleteId}`);

      if (String(value) === String(deleteId)) {
        onChange("");
      }

      setDeleteId("");
      setShowDeleteModal(false);

      await loadOptions();

      alert("Valeur de tarification supprimée.");
    } catch (error) {
      console.error("Erreur suppression valeur tarification :", error);
      alert(
        error?.message ||
          "Impossible de supprimer cette valeur. Elle est probablement utilisée."
      );
    } finally {
      setDeletingOption(false);
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
            value={value || ""}
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
            onClick={() => setShowAddModal(true)}
            className="inline-flex h-[50px] w-[50px] items-center justify-center rounded-xl border bg-white text-slate-700 hover:bg-slate-50"
            title="Ajouter une nouvelle valeur"
            disabled={!campgroundId}
          >
            <Plus className="h-5 w-5" />
          </button>

          <button
            type="button"
            onClick={() => setShowDeleteModal(true)}
            className="inline-flex h-[50px] w-[50px] items-center justify-center rounded-xl border bg-white text-red-700 hover:bg-red-50"
            title="Supprimer une valeur"
            disabled={!campgroundId || options.length === 0}
          >
            <Minus className="h-5 w-5" />
          </button>

          <button
            type="button"
            onClick={() => setShowHelpModal(true)}
            className="inline-flex h-[50px] w-[50px] items-center justify-center rounded-xl border bg-white text-blue-700 hover:bg-blue-50"
            title="Aide"
          >
            <HelpCircle className="h-5 w-5" />
          </button>
        </div>

        <p className="mt-1 text-xs text-slate-500">
          Cette valeur servira à regrouper les sites pour la tarification.
        </p>
      </div>

      {showAddModal && (
        <div className="fixed inset-0 z-[999] flex items-center justify-center bg-slate-900/50 px-4">
          <div className="w-full max-w-lg rounded-3xl bg-white shadow-2xl">
            <div className="flex items-start justify-between gap-4 border-b px-6 py-5">
              <div>
                <h3 className="text-lg font-bold text-slate-900">
                  Ajouter une valeur de tarification
                </h3>
                <p className="mt-1 text-sm text-slate-500">
                  Exemple : Standard, Premium, Bord de l’eau.
                </p>
              </div>

              <button
                type="button"
                onClick={() => {
                  setShowAddModal(false);
                  setNewName("");
                }}
                className="inline-flex h-10 w-10 items-center justify-center rounded-xl border text-slate-600 hover:bg-slate-50"
              >
                <X className="h-5 w-5" />
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
                  setShowAddModal(false);
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

      {showDeleteModal && (
        <div className="fixed inset-0 z-[999] flex items-center justify-center bg-slate-900/50 px-4">
          <div className="w-full max-w-lg rounded-3xl bg-white shadow-2xl">
            <div className="flex items-start justify-between gap-4 border-b px-6 py-5">
              <div>
                <h3 className="text-lg font-bold text-slate-900">
                  Supprimer une valeur de tarification
                </h3>
                <p className="mt-1 text-sm text-slate-500">
                  La suppression est permise seulement si la valeur n’est pas utilisée.
                </p>
              </div>

              <button
                type="button"
                onClick={() => {
                  setShowDeleteModal(false);
                  setDeleteId("");
                }}
                className="inline-flex h-10 w-10 items-center justify-center rounded-xl border text-slate-600 hover:bg-slate-50"
              >
                <X className="h-5 w-5" />
              </button>
            </div>

            <div className="space-y-4 px-6 py-5">
              <div className="rounded-2xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
                Si cette valeur est utilisée par un site, une règle tarifaire ou une
                promotion, le backend refusera la suppression.
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Valeur à supprimer
                </label>

                <select
                  value={deleteId}
                  onChange={(e) => setDeleteId(e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                >
                  <option value="">Choisir une valeur</option>

                  {options.map((item) => (
                    <option key={item.id} value={item.id}>
                      {item.name}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="flex justify-end gap-3 border-t px-6 py-4">
              <button
                type="button"
                onClick={() => {
                  setShowDeleteModal(false);
                  setDeleteId("");
                }}
                className="rounded-xl border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
              >
                Annuler
              </button>

              <button
                type="button"
                onClick={handleDeleteOption}
                disabled={deletingOption || !deleteId}
                className="inline-flex items-center gap-2 rounded-xl bg-red-600 px-4 py-2 text-sm font-medium text-white hover:bg-red-700 disabled:opacity-60"
              >
                <Trash2 className="h-4 w-4" />
                {deletingOption ? "Suppression..." : "Supprimer"}
              </button>
            </div>
          </div>
        </div>
      )}

      <CampsitePricingOptionHelpModal
        open={showHelpModal}
        onClose={() => setShowHelpModal(false)}
      />
    </>
  );
}
