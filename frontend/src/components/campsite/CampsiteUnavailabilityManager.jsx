
// ============================================================
// Fichier : frontend/src/components/campsite/CampsiteUnavailabilityManager.jsx
// Dernière modification : 2026-04-20
//
// Résumé :
// - Gestion des périodes d’indisponibilité temporaires d’un site
// - Permet d’ajouter, modifier et supprimer des périodes
// - Correction du rechargement automatique après sauvegarde
// - Compatible avec le service api du projet qui retourne directement les données
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { Pencil, Plus, RefreshCcw, Trash2, X } from "lucide-react";
import api from "../../services/api";

function emptyForm(campsiteId) {
  return {
    campsiteId,
    startDate: "",
    endDate: "",
    reason: "",
    isBlocking: true,
    notes: "",
  };
}

function formatDate(value) {
  if (!value) return "";
  return value;
}

export default function CampsiteUnavailabilityManager({ campsiteId }) {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);

  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState(emptyForm(campsiteId));
  const isEditing = editingId !== null;

  const canLoad = useMemo(() => Number.isFinite(Number(campsiteId)), [campsiteId]);

  useEffect(() => {
    setForm(emptyForm(campsiteId));
    setEditingId(null);

    if (canLoad) {
      loadItems();
    } else {
      setItems([]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [campsiteId]);

  async function loadItems() {
    if (!campsiteId) return;

    setLoading(true);
    try {
      const response = await api.get(`/campsite-unavailabilities/by-campsite/${campsiteId}`);

      const rows = Array.isArray(response)
        ? response
        : Array.isArray(response?.data)
        ? response.data
        : [];

      setItems(rows);
    } catch (error) {
      console.error("Erreur chargement indisponibilités :", error);
      alert("Impossible de charger les périodes d’indisponibilité.");
      setItems([]);
    } finally {
      setLoading(false);
    }
  }

  function resetForm() {
    setEditingId(null);
    setForm(emptyForm(campsiteId));
  }

  function startEdit(item) {
    setEditingId(item.id);
    setForm({
      campsiteId: item.campsiteId,
      startDate: formatDate(item.startDate),
      endDate: formatDate(item.endDate),
      reason: item.reason || "",
      isBlocking: item.isBlocking ?? true,
      notes: item.notes || "",
    });
  }

  function updateField(name, value) {
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!campsiteId) {
      alert("Tu dois d’abord enregistrer le site avant d’ajouter des indisponibilités.");
      return;
    }

    if (!form.startDate || !form.endDate) {
      alert("Les dates de début et de fin sont obligatoires.");
      return;
    }

    if (!form.reason.trim()) {
      alert("La raison est obligatoire.");
      return;
    }

    setSaving(true);

    try {
      const payload = {
        campsiteId: Number(campsiteId),
        startDate: form.startDate,
        endDate: form.endDate,
        reason: form.reason.trim(),
        isBlocking: !!form.isBlocking,
        notes: form.notes?.trim() || null,
      };

      if (isEditing) {
        await api.put(`/campsite-unavailabilities/${editingId}`, {
          startDate: payload.startDate,
          endDate: payload.endDate,
          reason: payload.reason,
          isBlocking: payload.isBlocking,
          notes: payload.notes,
        });
      } else {
        await api.post("/campsite-unavailabilities", payload);
      }

      resetForm();
      await loadItems();
    } catch (error) {
      console.error("Erreur sauvegarde indisponibilité :", error);
      alert(
        error?.response?.data?.message ||
          error?.response?.data?.error ||
          "Impossible de sauvegarder cette période."
      );
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(id) {
    const ok = window.confirm("Supprimer cette période d’indisponibilité ?");
    if (!ok) return;

    try {
      await api.delete(`/campsite-unavailabilities/${id}`);
      if (editingId === id) {
        resetForm();
      }
      await loadItems();
    } catch (error) {
      console.error("Erreur suppression indisponibilité :", error);
      alert("Impossible de supprimer cette période.");
    }
  }

  return (
    <div className="rounded-2xl border bg-white p-5 shadow-sm">
      <div className="mb-4 flex flex-wrap items-center justify-between gap-3">
        <div>
          <h3 className="text-lg font-semibold text-slate-900">
            Indisponibilités temporaires
          </h3>
          <p className="text-sm text-slate-500">
            Bloque la location d’un site pour une période précise.
          </p>
        </div>

        <button
          type="button"
          onClick={loadItems}
          className="inline-flex items-center gap-2 rounded-xl border px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
        >
          <RefreshCcw className="h-4 w-4" />
          Rafraîchir
        </button>
      </div>

      {!campsiteId ? (
        <div className="rounded-xl border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
          Enregistre d’abord le site pour pouvoir lui ajouter des périodes d’indisponibilité.
        </div>
      ) : (
        <>
          <form onSubmit={handleSubmit} className="mb-6 rounded-2xl border bg-slate-50 p-4">
            <div className="mb-4 flex items-center justify-between gap-3">
              <p className="font-semibold text-slate-900">
                {isEditing ? "Modifier une période" : "Ajouter une période"}
              </p>

              {isEditing && (
                <button
                  type="button"
                  onClick={resetForm}
                  className="inline-flex items-center gap-2 rounded-xl border px-3 py-2 text-sm font-medium text-slate-700 hover:bg-white"
                >
                  <X className="h-4 w-4" />
                  Annuler
                </button>
              )}
            </div>

            <div className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-4">
              <div>
                <label className="mb-1 block text-sm font-medium text-slate-700">
                  Date début
                </label>
                <input
                  type="date"
                  value={form.startDate}
                  onChange={(e) => updateField("startDate", e.target.value)}
                  className="w-full rounded-xl border px-3 py-2"
                />
              </div>

              <div>
                <label className="mb-1 block text-sm font-medium text-slate-700">
                  Date fin
                </label>
                <input
                  type="date"
                  value={form.endDate}
                  onChange={(e) => updateField("endDate", e.target.value)}
                  className="w-full rounded-xl border px-3 py-2"
                />
              </div>

              <div className="xl:col-span-2">
                <label className="mb-1 block text-sm font-medium text-slate-700">
                  Raison
                </label>
                <input
                  type="text"
                  value={form.reason}
                  onChange={(e) => updateField("reason", e.target.value)}
                  placeholder="Ex. borne électrique défectueuse"
                  maxLength={150}
                  className="w-full rounded-xl border px-3 py-2"
                />
              </div>

              <div className="xl:col-span-4">
                <label className="mb-1 block text-sm font-medium text-slate-700">
                  Notes
                </label>
                <textarea
                  value={form.notes}
                  onChange={(e) => updateField("notes", e.target.value)}
                  rows={3}
                  placeholder="Ex. visite de l’électricien prévue mardi prochain"
                  className="w-full rounded-xl border px-3 py-2"
                />
              </div>

              <div className="xl:col-span-4">
                <label className="inline-flex items-center gap-2 text-sm font-medium text-slate-700">
                  <input
                    type="checkbox"
                    checked={!!form.isBlocking}
                    onChange={(e) => updateField("isBlocking", e.target.checked)}
                  />
                  Bloquer la réservation pendant cette période
                </label>
              </div>
            </div>

            <div className="mt-4 flex flex-wrap items-center gap-3">
              <button
                type="submit"
                disabled={saving}
                className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-sm font-semibold text-white hover:bg-slate-800 disabled:opacity-60"
              >
                <Plus className="h-4 w-4" />
                {saving
                  ? "Sauvegarde..."
                  : isEditing
                  ? "Enregistrer les modifications"
                  : "Ajouter la période"}
              </button>
            </div>
          </form>

          <div className="overflow-x-auto rounded-2xl border">
            <table className="min-w-full divide-y divide-slate-200 text-sm">
              <thead className="bg-slate-50">
                <tr>
                  <th className="px-4 py-3 text-left font-semibold text-slate-700">Début</th>
                  <th className="px-4 py-3 text-left font-semibold text-slate-700">Fin</th>
                  <th className="px-4 py-3 text-left font-semibold text-slate-700">Raison</th>
                  <th className="px-4 py-3 text-left font-semibold text-slate-700">Blocage</th>
                  <th className="px-4 py-3 text-left font-semibold text-slate-700">Notes</th>
                  <th className="px-4 py-3 text-right font-semibold text-slate-700">Actions</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-slate-100 bg-white">
                {loading ? (
                  <tr>
                    <td colSpan={6} className="px-4 py-6 text-center text-slate-500">
                      Chargement...
                    </td>
                  </tr>
                ) : items.length === 0 ? (
                  <tr>
                    <td colSpan={6} className="px-4 py-6 text-center text-slate-500">
                      Aucune période d’indisponibilité.
                    </td>
                  </tr>
                ) : (
                  items.map((item) => (
                    <tr key={item.id}>
                      <td className="px-4 py-3">{item.startDate}</td>
                      <td className="px-4 py-3">{item.endDate}</td>
                      <td className="px-4 py-3 font-medium text-slate-900">{item.reason}</td>
                      <td className="px-4 py-3">
                        {item.isBlocking ? (
                          <span className="rounded-full bg-red-100 px-2.5 py-1 text-xs font-semibold text-red-700">
                            Oui
                          </span>
                        ) : (
                          <span className="rounded-full bg-slate-100 px-2.5 py-1 text-xs font-semibold text-slate-600">
                            Non
                          </span>
                        )}
                      </td>
                      <td className="px-4 py-3 text-slate-600">{item.notes || "-"}</td>
                      <td className="px-4 py-3">
                        <div className="flex justify-end gap-2">
                          <button
                            type="button"
                            onClick={() => startEdit(item)}
                            className="inline-flex items-center gap-2 rounded-xl border px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
                          >
                            <Pencil className="h-4 w-4" />
                            Modifier
                          </button>

                          <button
                            type="button"
                            onClick={() => handleDelete(item.id)}
                            className="inline-flex items-center gap-2 rounded-xl border border-red-200 px-3 py-2 text-sm font-medium text-red-700 hover:bg-red-50"
                          >
                            <Trash2 className="h-4 w-4" />
                            Supprimer
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}