// ============================================================
// Fichier : frontend/src/components/map/MapLegend.jsx
// Dernière modification : 2026-04-18
//
// Résumé :
// - Légende visuelle simple pour la carte
// ============================================================

import React from "react";

function LegendItem({ colorClass, label }) {
  return (
    <div className="flex items-center gap-3">
      <span className={`inline-block h-4 w-4 rounded ${colorClass}`} />
      <span className="text-sm text-slate-700">{label}</span>
    </div>
  );
}

export default function MapLegend() {
  return (
    <div className="bg-white rounded-3xl shadow-sm border p-6">
      <h2 className="text-lg font-semibold text-slate-900 mb-4">Légende</h2>

      <div className="space-y-3">
        <LegendItem colorClass="bg-blue-400" label="Site actif" />
        <LegendItem colorClass="bg-slate-400" label="Site inactif" />
        <LegendItem colorClass="bg-emerald-400" label="Élément de carte actif" />
      </div>

      <p className="text-sm text-slate-500 mt-5">
        Cliquez sur un site pour ouvrir sa page de modification.
      </p>
    </div>
  );
}