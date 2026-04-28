// ============================================================
// Fichier : frontend/src/components/pricing/PricingDaysSelector.jsx
// Dernière modification : 2026-04-23
//
// Résumé :
// - Sélecteur des jours applicables à une règle tarifaire
// - Si aucun jour n’est sélectionné, la règle peut être considérée
//   comme applicable tous les jours côté backend
// ============================================================

import React from "react";
import { PRICING_DAYS } from "../../constants/pricingDays";

export default function PricingDaysSelector({ selectedDays, onChange }) {
  const safeSelectedDays = Array.isArray(selectedDays) ? selectedDays : [];

  const toggleDay = (day) => {
    if (safeSelectedDays.includes(day)) {
      onChange(safeSelectedDays.filter((x) => x !== day));
      return;
    }

    onChange([...safeSelectedDays, day]);
  };

  const selectAll = () => {
    onChange(PRICING_DAYS.map((day) => day.value));
  };

  const clearAll = () => {
    onChange([]);
  };

  return (
    <div className="rounded-2xl border bg-white p-5">
      <div className="mb-4 flex flex-wrap items-center justify-between gap-3">
        <div>
          <h3 className="text-lg font-semibold text-slate-900">
            Jours applicables
          </h3>
          <p className="text-sm text-slate-500">
            Choisis les jours visés par cette règle.
          </p>
        </div>

        <div className="flex gap-2">
          <button
            type="button"
            onClick={selectAll}
            className="rounded-xl border px-3 py-2 text-sm hover:bg-slate-50"
          >
            Tous
          </button>

          <button
            type="button"
            onClick={clearAll}
            className="rounded-xl border px-3 py-2 text-sm hover:bg-slate-50"
          >
            Aucun
          </button>
        </div>
      </div>

      <div className="grid gap-3 md:grid-cols-4">
        {PRICING_DAYS.map((day) => (
          <label
            key={day.value}
            className="inline-flex cursor-pointer items-center gap-3 rounded-xl border px-4 py-3 hover:bg-slate-50"
          >
            <input
              type="checkbox"
              checked={safeSelectedDays.includes(day.value)}
              onChange={() => toggleDay(day.value)}
              className="h-4 w-4"
            />
            <span className="text-sm text-slate-700">{day.label}</span>
          </label>
        ))}
      </div>
    </div>
  );
}
