// ============================================================
// Fichier : SearchAdvancedFilters.jsx
// Chemin  : frontend/src/components/reservation
// Dernière modification : 2026-05-09
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Panneau "+ de filtres" pour la recherche de terrains
// - Contient les filtres secondaires :
//   accès direct, surfaces, services du camping et activités
// - Utilise la liste complète des services et activités de la BD
//
// Historique des modifications :
// 2026-05-09
// - Création initiale du composant
// - Retrait eau/égout/ampérage du panneau avancé
// - Ajout services camping complets
// - Ajout activités camping complètes
// ============================================================

import React from "react";

const CAMPGROUND_SERVICES = [
  { code: "DRINKING_WATER", label: "Eau potable" },
  { code: "ELECTRICITY", label: "Prise avec électricité" },
  { code: "TOILETS", label: "Toilettes" },
  { code: "SHOWERS", label: "Douches" },
  { code: "LAUNDRY", label: "Buanderie" },
  { code: "WOOD", label: "Bois disponible" },
  { code: "ICE", label: "Glace disponible" },
  { code: "PROPANE", label: "Propane disponible" },
  { code: "WIFI", label: "Wi-Fi disponible" },
  { code: "WIFI_FREE", label: "Wi-Fi gratuit" },
  { code: "RECYCLING", label: "Collecte de matières recyclables" },
  { code: "ORGANIC_WASTE", label: "Collecte de matières organiques" },
  { code: "EV_CHARGING", label: "Borne de recharge électrique" },
  { code: "DUMP_STATION", label: "Station de vidange" },
  { code: "ACCESSIBLE", label: "Accessible" },
  { code: "PARTIALLY_ACCESSIBLE", label: "Partiellement accessible" },
  { code: "CONVENIENCE_STORE", label: "Dépanneur sur place" },
  { code: "RESTAURANT", label: "Restaurant sur place" },
  { code: "SNACK_BAR", label: "Casse-croûte sur place" },
  { code: "BOAT_RAMP", label: "Rampe de mise à l’eau" },
  { code: "MARINA_GAS", label: "Marina avec essence" },
  { code: "PICNIC_AREA", label: "Aire de pique-nique" },
];

const CAMPGROUND_ACTIVITIES = [
  { code: "BEACH", label: "Plage" },
  { code: "SWIMMING_POOL", label: "Piscine" },
  { code: "HEATED_POOL", label: "Piscine chauffée" },
  { code: "WADING_POOL", label: "Pataugeoire" },
  { code: "WATER_GAMES", label: "Jeux d’eau" },
  { code: "WATER_SLIDES", label: "Glissades d’eau" },
  { code: "SPA", label: "SPA" },
  { code: "PLAYGROUND", label: "Terrain de jeux" },
  { code: "ORGANIZED_ACTIVITIES", label: "Loisirs organisés" },
  { code: "GAME_ROOM", label: "Salle de jeux" },
  { code: "MOVIE_PROJECTION", label: "Projection de films" },
  { code: "LIBRARY", label: "Bibliothèque" },
  { code: "INTERPRETATION_CENTER", label: "Centre d’interprétation" },
  { code: "PETANQUE", label: "Fer / pétanque / croquet" },
  { code: "SKATEPARK", label: "Planche à roulettes" },
  { code: "TRAMPOLINE", label: "Trampoline" },
  { code: "BADMINTON", label: "Badminton" },
  { code: "BASKETBALL", label: "Basketball" },
  { code: "FITNESS_CENTER", label: "Centre de conditionnement physique" },
  { code: "HOCKEY", label: "Hockey / dek hockey" },
  { code: "TENNIS", label: "Tennis" },
  { code: "PICKLEBALL", label: "Pickleball" },
  { code: "BASEBALL", label: "Terrain de balle" },
  { code: "SOCCER", label: "Terrain de soccer" },
  { code: "VOLLEYBALL", label: "Volleyball" },
  { code: "CLIMBING", label: "Escalade" },
  { code: "LOOKOUT", label: "Belvédère" },
  { code: "BIKE_RENTAL", label: "Location de vélo" },
  { code: "MINIGOLF", label: "Minigolf" },
  { code: "BMX", label: "Piste de BMX" },
  { code: "BIKE_PATH", label: "Piste cyclable" },
  { code: "HORSEBACK_RIDING", label: "Équitation" },
  { code: "GOLF", label: "Golf" },
  { code: "FISHING", label: "Pêche" },
  { code: "ATV", label: "VTT" },
  { code: "HIKING", label: "Randonnée pédestre" },
];

const SURFACE_TYPES = [
  "Gazon",
  "Gravier",
  "Béton",
  "Sable",
  "Terre",
];

export default function SearchAdvancedFilters({ filters, onChange }) {
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
      <h3 className="mb-2 text-xl font-bold">
        + de filtres
      </h3>

      <p className="mb-5 text-sm text-gray-600">
        Ces options permettent de préciser l’expérience recherchée, sans alourdir la recherche de base.
      </p>

      <div className="grid gap-6 md:grid-cols-2">
        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Accès au terrain
          </h4>

          <label className="flex items-center gap-2 text-sm">
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

        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Surface du site
          </h4>

          <div className="space-y-2 text-sm">
            {SURFACE_TYPES.map((surface) => (
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

        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Services du camping
          </h4>

          <div className="grid gap-2 text-sm">
            {CAMPGROUND_SERVICES.map((service) => (
              <label key={service.code} className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={(filters.campgroundServiceCodes || []).includes(
                    service.code
                  )}
                  onChange={() =>
                    toggleArrayValue("campgroundServiceCodes", service.code)
                  }
                />
                {service.label}
              </label>
            ))}
          </div>
        </div>

        <div>
          <h4 className="mb-3 font-semibold text-gray-800">
            Activités souhaitées
          </h4>

          <div className="grid gap-2 text-sm">
            {CAMPGROUND_ACTIVITIES.map((activity) => (
              <label key={activity.code} className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={(filters.activityCodes || []).includes(
                    activity.code
                  )}
                  onChange={() =>
                    toggleArrayValue("activityCodes", activity.code)
                  }
                />
                {activity.label}
              </label>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}