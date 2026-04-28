// ============================================================
// Fichier : frontend/src/components/pricing/PricingAmountFields.jsx
// Dernière modification : 2026-04-23
//
// Résumé :
// - Champs de prix pour règle FIXED ou DYNAMIC
// - Ajout du champ Tarif de référence dynamique
// ============================================================

import React from "react";

export default function PricingAmountFields({ form, updateField }) {
  const isDynamic = form.pricingType === "DYNAMIC";
  const isFixed = form.pricingType === "FIXED";

  return (
    <div className="rounded-2xl border bg-white p-5">
      <h3 className="text-lg font-semibold text-slate-900 mb-4">
        Montants
      </h3>

      {isFixed && (
        <NumberField
          label="Prix fixe"
          value={form.fixedPrice}
          onChange={(value) => updateField("fixedPrice", value)}
        />
      )}

      {isDynamic && (
        <div className="grid gap-4 md:grid-cols-3">
          <NumberField
            label="Tarif minimum"
            value={form.dynamicMinPrice}
            onChange={(value) => updateField("dynamicMinPrice", value)}
          />

          <NumberField
            label="Tarif de référence"
            value={form.dynamicBasePrice}
            onChange={(value) => updateField("dynamicBasePrice", value)}
          />

          <NumberField
            label="Tarif maximum"
            value={form.dynamicMaxPrice}
            onChange={(value) => updateField("dynamicMaxPrice", value)}
          />
        </div>
      )}
    </div>
  );
}

function NumberField({ label, value, onChange }) {
  return (
    <div>
      <label className="mb-1 block text-sm font-medium text-slate-700">
        {label}
      </label>

      <input
        type="number"
        min="0"
        step="0.01"
        value={value ?? ""}
        onChange={(e) => onChange(e.target.value)}
        className="w-full rounded-xl border px-4 py-3"
      />
    </div>
  );
}