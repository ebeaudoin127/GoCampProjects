// ============================================================
// Fichier : EquipementsTab.jsx
// Dernière modification : 2026-04-16
// Résumé des modifications :
// - Ajout d'une colonne "Sélection"
// - Suppression de la colonne "Verrou"
// - Affichage du cadenas verrouillé/déverrouillé à gauche de la marque
// - Conservation du hint (tooltip) au survol du cadenas
// - Mise en évidence beaucoup plus claire de la ligne sélectionnée
// - Affichage d'un encadré "Équipement sélectionné"
// - Bouton Modifier l’équipement sélectionné
// - Bouton Désactiver un équipement
// - Préparation de la logique future hasFutureReservations sans sauvegarde en base
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { Lock, Unlock } from "lucide-react";
import api from "../services/api";

const emptyForm = {
  marque: "",
  modele: "",
  longueur: "",
  noSerie: "",
  noPlaque: "",
  hasExtension: false,
  extensionConducteur: "",
  extensionPassager: "",
  actif: true,
};

export default function EquipementsTab() {
  const [equipements, setEquipements] = useState([]);
  const [selectedId, setSelectedId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);

  const [form, setForm] = useState(emptyForm);

  const selectedEquipement = useMemo(
    () => equipements.find((e) => e.id === selectedId) || null,
    [equipements, selectedId]
  );

  const selectedIsLocked = selectedEquipement?.hasFutureReservations ?? false;

  const loadEquipements = async () => {
    setLoading(true);
    setMessage("");

    try {
      const data = await api.get("/users/me/equipements");
      setEquipements(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Erreur chargement équipements :", err);
      setMessage("Erreur lors du chargement des équipements.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadEquipements();
  }, []);

  const updateField = (field, value) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

  const fillFormFromEquipement = (equipement) => {
    setForm({
      marque: equipement.marque || "",
      modele: equipement.modele || "",
      longueur: equipement.longueur ?? "",
      noSerie: equipement.noSerie || "",
      noPlaque: equipement.noPlaque || "",
      hasExtension: Boolean(equipement.hasExtension),
      extensionConducteur: equipement.extensionConducteur ?? "",
      extensionPassager: equipement.extensionPassager ?? "",
      actif: equipement.actif ?? true,
    });
  };

  const handleAddNew = () => {
    setSelectedId(null);
    setIsEditing(false);
    setShowForm(true);
    setForm(emptyForm);
    setMessage("");
  };

  const handleEditSelected = () => {
    if (!selectedEquipement) {
      setMessage("Sélectionne un équipement dans la grille.");
      return;
    }

    if (selectedIsLocked) {
      setMessage(
        "Cet équipement ne peut pas être modifié, car il a des réservations futures."
      );
      return;
    }

    setIsEditing(true);
    setShowForm(true);
    setMessage("");
    fillFormFromEquipement(selectedEquipement);
  };

  const handleCancel = () => {
    setSelectedId(null);
    setIsEditing(false);
    setShowForm(false);
    setForm(emptyForm);
    setMessage("");
  };

  const handleSave = async () => {
    setMessage("");

    const payload = {
      marque: form.marque || null,
      modele: form.modele || null,
      longueur: form.longueur !== "" ? Number(form.longueur) : null,
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
      if (isEditing && selectedId) {
        await api.put(`/users/me/equipements/${selectedId}`, payload);
        setMessage("Équipement mis à jour.");
      } else {
        await api.post("/users/me/equipements", payload);
        setMessage("Équipement ajouté.");
      }

      await loadEquipements();
      setShowForm(false);
      setIsEditing(false);
      setSelectedId(null);
      setForm(emptyForm);
    } catch (err) {
      console.error("Erreur sauvegarde équipement :", err);
      setMessage(
        err?.response?.data ||
          "Erreur lors de l'enregistrement de l'équipement."
      );
    }
  };

  const handleDeactivate = async () => {
    if (!selectedEquipement) {
      setMessage("Sélectionne un équipement dans la grille.");
      return;
    }

    if (selectedEquipement.actif === false) {
      setMessage("Cet équipement est déjà désactivé.");
      return;
    }

    try {
      await api.put(`/users/me/equipements/${selectedEquipement.id}`, {
        marque: selectedEquipement.marque || null,
        modele: selectedEquipement.modele || null,
        longueur:
          selectedEquipement.longueur !== null &&
          selectedEquipement.longueur !== undefined
            ? Number(selectedEquipement.longueur)
            : null,
        noSerie: selectedEquipement.noSerie || null,
        noPlaque: selectedEquipement.noPlaque || null,
        hasExtension: Boolean(selectedEquipement.hasExtension),
        extensionConducteur:
          selectedEquipement.hasExtension &&
          selectedEquipement.extensionConducteur !== null &&
          selectedEquipement.extensionConducteur !== undefined
            ? Number(selectedEquipement.extensionConducteur)
            : null,
        extensionPassager:
          selectedEquipement.hasExtension &&
          selectedEquipement.extensionPassager !== null &&
          selectedEquipement.extensionPassager !== undefined
            ? Number(selectedEquipement.extensionPassager)
            : null,
        actif: false,
      });

      setMessage("Équipement désactivé.");
      await loadEquipements();
    } catch (err) {
      console.error("Erreur désactivation équipement :", err);
      setMessage(err?.response?.data || "Erreur lors de la désactivation.");
    }
  };

  const formatExtensionValue = (equipement, sideValue) => {
    if (!equipement.hasExtension) return "-";
    if (sideValue === null || sideValue === undefined || sideValue === "") {
      return "0";
    }
    return sideValue;
  };

  const selectedSummary = selectedEquipement
    ? [
        selectedEquipement.marque || "Sans marque",
        selectedEquipement.modele || "Sans modèle",
        selectedEquipement.noPlaque
          ? `Plaque : ${selectedEquipement.noPlaque}`
          : null,
      ]
        .filter(Boolean)
        .join(" • ")
    : "Aucun équipement sélectionné";

  if (loading) {
    return <div className="text-center py-8">Chargement...</div>;
  }

  return (
    <div className="space-y-6">
      {message && (
        <div className="rounded-lg border border-orange-200 bg-orange-50 px-4 py-3 text-orange-700">
          {message}
        </div>
      )}

      <div className="overflow-x-auto rounded-xl border border-gray-200">
        <table className="min-w-full text-sm">
          <thead className="bg-gray-100 text-gray-700">
            <tr>
              <th className="px-4 py-3 text-left">Sélection</th>
              <th className="px-4 py-3 text-left">Marque</th>
              <th className="px-4 py-3 text-left">Modèle</th>
              <th className="px-4 py-3 text-left">Longueur</th>
              <th className="px-4 py-3 text-left">No série</th>
              <th className="px-4 py-3 text-left">No plaque</th>
              <th className="px-4 py-3 text-left">Extension</th>
              <th className="px-4 py-3 text-left">Nb ext. conducteur</th>
              <th className="px-4 py-3 text-left">Nb ext. passager</th>
              <th className="px-4 py-3 text-left">État</th>
            </tr>
          </thead>

          <tbody>
            {equipements.length === 0 ? (
              <tr>
                <td
                  colSpan="10"
                  className="px-4 py-6 text-center text-gray-500 bg-white"
                >
                  Aucun équipement enregistré.
                </td>
              </tr>
            ) : (
              equipements.map((equipement) => {
                const isSelected = selectedId === equipement.id;
                const hasFutureReservations =
                  equipement.hasFutureReservations ?? false;

                return (
                  <tr
                    key={equipement.id}
                    onClick={() => setSelectedId(equipement.id)}
                    className={`cursor-pointer border-t transition ${
                      isSelected
                        ? "bg-orange-100 ring-2 ring-inset ring-orange-500"
                        : "bg-white hover:bg-orange-50"
                    }`}
                  >
                    <td className="px-4 py-3">
                      <div
                        className={`flex h-5 w-5 items-center justify-center rounded-full border-2 ${
                          isSelected
                            ? "border-orange-600 bg-orange-600 text-white"
                            : "border-gray-400 bg-white"
                        }`}
                      >
                        {isSelected ? "✓" : ""}
                      </div>
                    </td>

                    <td className={`px-4 py-3 ${isSelected ? "font-bold" : ""}`}>
                      <div className="flex items-center gap-2">
                        <span
                          title={
                            hasFutureReservations
                              ? "Cet équipement a des réservations futures actives."
                              : "Aucune réservation future active."
                          }
                          className={`flex h-6 w-6 items-center justify-center rounded-full ${
                            hasFutureReservations
                              ? "bg-red-100"
                              : "bg-green-100"
                          }`}
                        >
                          {hasFutureReservations ? (
                            <Lock className="h-3.5 w-3.5 text-red-600" />
                          ) : (
                            <Unlock className="h-3.5 w-3.5 text-green-600" />
                          )}
                        </span>

                        <span>{equipement.marque || "-"}</span>
                      </div>
                    </td>

                    <td className={`px-4 py-3 ${isSelected ? "font-bold" : ""}`}>
                      {equipement.modele || "-"}
                    </td>

                    <td className="px-4 py-3">
                      {equipement.longueur ? `${equipement.longueur} pi` : "-"}
                    </td>

                    <td className="px-4 py-3">{equipement.noSerie || "-"}</td>
                    <td className="px-4 py-3">{equipement.noPlaque || "-"}</td>

                    <td className="px-4 py-3">
                      {equipement.hasExtension ? "Oui" : "Non"}
                    </td>

                    <td className="px-4 py-3">
                      {formatExtensionValue(
                        equipement,
                        equipement.extensionConducteur
                      )}
                    </td>

                    <td className="px-4 py-3">
                      {formatExtensionValue(
                        equipement,
                        equipement.extensionPassager
                      )}
                    </td>

                    <td className="px-4 py-3">
                      <span
                        className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${
                          equipement.actif
                            ? "bg-green-100 text-green-700"
                            : "bg-gray-200 text-gray-600"
                        }`}
                      >
                        {equipement.actif ? "Actif" : "Désactivé"}
                      </span>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>

      <div className="rounded-lg border border-blue-200 bg-blue-50 px-4 py-3 text-blue-800">
        <span className="font-semibold">Équipement sélectionné :</span>{" "}
        {selectedSummary}
        {selectedEquipement && (
          <span className="ml-2">
            {selectedIsLocked ? "• 🔒 Verrouillé" : "• 🔓 Déverrouillé"}
          </span>
        )}
      </div>

      <div className="flex flex-wrap gap-3">
        <button
          onClick={handleAddNew}
          className="rounded-lg bg-blue-600 px-5 py-3 font-semibold text-white hover:bg-blue-700"
        >
          Ajouter un équipement
        </button>

        <button
          onClick={handleEditSelected}
          className={`rounded-lg px-5 py-3 font-semibold text-white ${
            selectedEquipement && !selectedIsLocked
              ? "bg-amber-500 hover:bg-amber-600"
              : "bg-amber-300 cursor-not-allowed"
          }`}
          disabled={!selectedEquipement || selectedIsLocked}
          title={
            selectedIsLocked
              ? "Modification impossible : cet équipement a des réservations futures."
              : ""
          }
        >
          Modifier l’équipement sélectionné
        </button>

        <button
          onClick={handleDeactivate}
          className={`rounded-lg px-5 py-3 font-semibold text-white ${
            selectedEquipement
              ? "bg-gray-700 hover:bg-gray-800"
              : "bg-gray-400 cursor-not-allowed"
          }`}
          disabled={!selectedEquipement}
        >
          Désactiver un équipement
        </button>
      </div>

      {showForm && (
        <div className="rounded-2xl border border-gray-200 bg-gray-50 p-6">
          <h3 className="mb-4 text-xl font-bold">
            {isEditing ? "Modifier l’équipement" : "Ajouter un équipement"}
          </h3>

          <div className="grid gap-4 md:grid-cols-2">
            <input
              className="border p-3 w-full rounded bg-white"
              value={form.marque}
              onChange={(e) => updateField("marque", e.target.value)}
              placeholder="Marque"
            />

            <input
              className="border p-3 w-full rounded bg-white"
              value={form.modele}
              onChange={(e) => updateField("modele", e.target.value)}
              placeholder="Modèle"
            />

            <input
              type="number"
              className="border p-3 w-full rounded bg-white"
              value={form.longueur}
              onChange={(e) => updateField("longueur", e.target.value)}
              placeholder="Longueur (pieds)"
            />

            <input
              className="border p-3 w-full rounded bg-white"
              value={form.noSerie}
              onChange={(e) => updateField("noSerie", e.target.value)}
              placeholder="Numéro de série"
            />

            <input
              className="border p-3 w-full rounded bg-white md:col-span-2"
              value={form.noPlaque}
              onChange={(e) => updateField("noPlaque", e.target.value)}
              placeholder="Numéro de plaque"
            />
          </div>

          <div className="mt-4">
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={form.hasExtension}
                onChange={(e) => updateField("hasExtension", e.target.checked)}
              />
              <span>A des extensions (Slide-Out)</span>
            </label>
          </div>

          {form.hasExtension && (
            <div className="mt-4 grid gap-4 md:grid-cols-2">
              <input
                type="number"
                className="border p-3 w-full rounded bg-white"
                value={form.extensionConducteur}
                onChange={(e) =>
                  updateField("extensionConducteur", e.target.value)
                }
                placeholder="Nombre extension conducteur"
              />

              <input
                type="number"
                className="border p-3 w-full rounded bg-white"
                value={form.extensionPassager}
                onChange={(e) =>
                  updateField("extensionPassager", e.target.value)
                }
                placeholder="Nombre extension passager"
              />
            </div>
          )}

          <div className="mt-4">
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={form.actif}
                onChange={(e) => updateField("actif", e.target.checked)}
              />
              <span>Équipement actif</span>
            </label>
          </div>

          <div className="mt-6 flex flex-wrap gap-3">
            <button
              onClick={handleSave}
              className="rounded-lg bg-blue-600 px-5 py-3 font-semibold text-white hover:bg-blue-700"
            >
              Sauvegarder
            </button>

            <button
              onClick={handleCancel}
              className="rounded-lg border border-gray-300 bg-white px-5 py-3 font-semibold text-gray-700 hover:bg-gray-100"
            >
              Annuler
            </button>
          </div>
        </div>
      )}
    </div>
  );
}