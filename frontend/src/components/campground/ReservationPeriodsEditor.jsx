// ============================================================
// Fichier : ReservationPeriodsEditor.jsx
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Éditeur des périodes de réservation d'un camping
// - Permet plusieurs périodes sur plusieurs années
// - Valide les dates et les chevauchements côté frontend
// ============================================================

import React, { useMemo } from "react";

const createEmptyPeriod = () => ({
  id: null,
  startDate: "",
  endDate: "",
  active: true,
});

function sortPeriods(periods) {
  return [...(periods || [])].sort((a, b) => {
    const aDate = a.startDate || "9999-12-31";
    const bDate = b.startDate || "9999-12-31";
    return aDate.localeCompare(bDate);
  });
}

export function validateReservationPeriods(periods) {
  const errors = [];
  const normalized = sortPeriods(
    (periods || []).map((period, index) => ({
      ...period,
      originalIndex: index,
    }))
  );

  normalized.forEach((period) => {
    if (!period.startDate) {
      errors.push(`La période #${period.originalIndex + 1} n'a pas de date de début.`);
    }

    if (!period.endDate) {
      errors.push(`La période #${period.originalIndex + 1} n'a pas de date de fin.`);
    }

    if (period.startDate && period.endDate && period.endDate < period.startDate) {
      errors.push(`La période #${period.originalIndex + 1} a une date de fin avant la date de début.`);
    }
  });

  for (let i = 0; i < normalized.length; i += 1) {
    const current = normalized[i];

    if (!current.startDate || !current.endDate) continue;

    for (let j = i + 1; j < normalized.length; j += 1) {
      const next = normalized[j];

      if (!next.startDate || !next.endDate) continue;

      const overlaps =
        current.endDate >= next.startDate &&
        next.endDate >= current.startDate;

      if (overlaps) {
        errors.push(
          `La période #${current.originalIndex + 1} chevauche la période #${next.originalIndex + 1}.`
        );
      }
    }
  }

  return errors;
}

export default function ReservationPeriodsEditor({ value, onChange }) {
  const periods = Array.isArray(value) ? value : [];

  const errors = useMemo(() => validateReservationPeriods(periods), [periods]);

  const updatePeriod = (index, field, fieldValue) => {
    const next = periods.map((period, currentIndex) => {
      if (currentIndex !== index) return period;

      return {
        ...period,
        [field]: fieldValue,
      };
    });

    onChange(next);
  };

  const addPeriod = () => {
    onChange([...periods, createEmptyPeriod()]);
  };

  const removePeriod = (index) => {
    onChange(periods.filter((_, currentIndex) => currentIndex !== index));
  };

  const sortInUi = () => {
    onChange(sortPeriods(periods));
  };

  return (
    <div className="bg-white rounded-3xl shadow-sm border p-6">
      <div className="mb-4 flex flex-col gap-3 md:flex-row md:items-start md:justify-between">
        <div>
          <h2 className="text-xl font-semibold text-slate-900">
            Périodes de réservation
          </h2>
          <p className="text-sm text-slate-600 mt-1">
            Définissez les périodes pendant lesquelles les clients peuvent réserver ce camping.
          </p>
        </div>

        <div className="flex flex-wrap gap-2">
          {periods.length > 1 && (
            <button
              type="button"
              onClick={sortInUi}
              className="rounded-xl bg-white border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
            >
              Trier
            </button>
          )}

          <button
            type="button"
            onClick={addPeriod}
            className="rounded-xl bg-emerald-600 px-4 py-2 text-sm font-medium text-white hover:bg-emerald-700"
          >
            Ajouter une période
          </button>
        </div>
      </div>

      {periods.length === 0 ? (
        <div className="rounded-2xl border border-dashed bg-slate-50 px-4 py-4 text-sm text-slate-600">
          Aucune période définie. Le camping pourra être créé, mais les réservations devront être bloquées
          tant qu’aucune période active ne couvre les dates demandées.
        </div>
      ) : (
        <div className="space-y-3">
          {periods.map((period, index) => (
            <div
              key={period.id || index}
              className="grid gap-3 rounded-2xl border bg-slate-50 p-4 md:grid-cols-[1fr_1fr_auto_auto]"
            >
              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Date début
                </label>
                <input
                  type="date"
                  value={period.startDate || ""}
                  onChange={(e) => updatePeriod(index, "startDate", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                  Date fin
                </label>
                <input
                  type="date"
                  value={period.endDate || ""}
                  onChange={(e) => updatePeriod(index, "endDate", e.target.value)}
                  className="w-full rounded-xl border px-4 py-3"
                />
              </div>

              <label className="inline-flex items-center gap-3 pt-8 text-sm font-medium text-slate-700">
                <input
                  type="checkbox"
                  checked={period.active !== false}
                  onChange={(e) => updatePeriod(index, "active", e.target.checked)}
                  className="h-4 w-4"
                />
                Active
              </label>

              <div className="flex items-end">
                <button
                  type="button"
                  onClick={() => removePeriod(index)}
                  className="w-full rounded-xl border border-red-200 px-4 py-3 text-sm font-medium text-red-700 hover:bg-red-50"
                >
                  Supprimer
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {errors.length > 0 && (
        <div className="mt-4 rounded-2xl border border-amber-200 bg-amber-50 px-4 py-4">
          <p className="text-sm font-semibold text-amber-900 mb-2">
            À corriger avant l’enregistrement :
          </p>
          <ul className="list-disc pl-5 text-sm text-amber-800 space-y-1">
            {errors.map((error) => (
              <li key={error}>{error}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}