// ============================================================
// Fichier : SearchAdvancedFilters.jsx
// Chemin  : frontend/src/components/reservation
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Panneau de filtres avancés pour la recherche de terrains
// - Services requis
// - Accès direct au site
// - Surface du site
// - Activités souhaitées
//
// Historique des modifications :
// 2026-05-09
// - Création initiale du composant
// ============================================================

import React from "react";

export default function SearchAdvancedFilters({
  filters,
  onChange,
}) {
  const updateFilter = (field, value) => {
    onChange({
      ...filters,
      [field]: value,
    });
  };

  const toggleArrayValue = (field, value) => {
    const currentValues = filters[field] || [];

    const nextValues = currentValues.includes(value)
      ? currentValues.filter((item) => item !== value)
      : [...currentValues, value];

    updateFilter(field, nextValues);
  };

  return (
    <div className="mt-5 rounded-2xl border border-gray-200 bg-white p-5 shadow-sm">
      <h3 className="mb-4 text-xl font-bold">
        Filtres avancés
      </h3>

      <div className="grid gap-6 md:grid-cols-2">
        {/* SERVICES */}
        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Services du terrain
          </h4>

          <div className="space-y-2 text-sm">
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={filters.requiresWater}
                onChange={(e) =>
                  updateFilter("requiresWater", e.target.checked)
                }
              />
              Eau
            </label>

            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={filters.requiresElectricity}
                onChange={(e) =>
                  updateFilter("requiresElectricity", e.target.checked)
                }
              />
              Électricité
            </label>

            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={filters.requiresSewer}
                onChange={(e) =>
                  updateFilter("requiresSewer", e.target.checked)
                }
              />
              Égout
            </label>
          </div>
        </div>

        {/* ACCÈS */}
        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Accès au terrain
          </h4>

          <div className="space-y-2 text-sm">
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                checked={filters.pullThroughOnly}
                onChange={(e) =>
                  updateFilter("pullThroughOnly", e.target.checked)
                }
              />
              Accès direct / pull-through seulement
            </label>
          </div>
        </div>

        {/* SURFACE */}
        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Surface du site
          </h4>

          <div className="space-y-2 text-sm">
            {["Gazon", "Gravier", "Béton", "Sable", "Terre"].map((surface) => (
              <label key={surface} className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={(filters.surfaceTypes || []).includes(surface)}
                  onChange={() => toggleArrayValue("surfaceTypes", surface)}
                />
                {surface}
              </label>
            ))}
          </div>
        </div>

        {/* ACTIVITÉS */}
        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Activités souhaitées
          </h4>

          <div className="space-y-2 text-sm">
            {["Piscine", "Plage", "Randonnée", "Vélo", "Pêche"].map(
              (activity) => (
                <label key={activity} className="flex items-center gap-2">
                  <input
                    type="checkbox"
                    checked={(filters.activities || []).includes(activity)}
                    onChange={() => toggleArrayValue("activities", activity)}
                  />
                  {activity}
                </label>
              )
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
