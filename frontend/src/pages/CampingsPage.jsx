// ============================================================
// Fichier : CampingsPage.jsx
// Chemin  : frontend/src/pages
// Dernière modification : 2026-05-12
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Page publique listant tous les campings
// - Affichage sous forme de cartes
// - Recherche par nom, ville, province ou description
// - Boutons Voir le camping et Réserver
//
// Historique des modifications :
// 2026-05-12
// - Création initiale de la page publique Campings
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { MapPin, Search, Tent, Wifi, CalendarDays } from "lucide-react";
import { Link } from "react-router-dom";
import api from "../services/api";

export default function CampingsPage() {
  const [campgrounds, setCampgrounds] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    loadCampgrounds();
  }, []);

  async function loadCampgrounds() {
    setLoading(true);
    setError("");

    try {
      const data = await api.get("/campgrounds");
      setCampgrounds(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger la liste des campings.");
    } finally {
      setLoading(false);
    }
  }

  const filteredCampgrounds = useMemo(() => {
    const q = search.trim().toLowerCase();

    if (!q) {
      return campgrounds;
    }

    return campgrounds.filter((campground) => {
      const haystack = [
        campground.name,
        campground.city,
        campground.shortDescription,
        campground.longDescription,
        campground.postalCode,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      return haystack.includes(q);
    });
  }, [campgrounds, search]);

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="mx-auto max-w-7xl px-6 py-10">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-slate-900">
            Tous les campings
          </h1>

          <p className="mt-3 max-w-3xl text-slate-600">
            Explore les campings disponibles sur GoCamp et trouve rapidement un
            endroit adapté à ton équipement, tes dates et tes préférences.
          </p>
        </div>

        <div className="mb-8 rounded-3xl border bg-white p-5 shadow-sm">
          <div className="relative max-w-xl">
            <Search className="absolute left-4 top-1/2 h-5 w-5 -translate-y-1/2 text-slate-400" />

            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Rechercher par nom, ville ou région..."
              className="w-full rounded-2xl border px-12 py-4 text-sm outline-none focus:border-orange-500"
            />
          </div>
        </div>

        {loading && (
          <div className="rounded-2xl border bg-white p-6 text-slate-600">
            Chargement des campings...
          </div>
        )}

        {error && (
          <div className="rounded-2xl border border-red-200 bg-red-50 p-6 text-red-700">
            {error}
          </div>
        )}

        {!loading && !error && filteredCampgrounds.length === 0 && (
          <div className="rounded-2xl border bg-white p-6 text-slate-600">
            Aucun camping trouvé.
          </div>
        )}

        {!loading && !error && filteredCampgrounds.length > 0 && (
          <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {filteredCampgrounds.map((campground) => (
              <CampgroundCard
                key={campground.id}
                campground={campground}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

function CampgroundCard({ campground }) {
  const totalSites = campground.totalSites ?? 0;
  const travelerSitesCount = campground.travelerSitesCount ?? 0;

  return (
    <div className="overflow-hidden rounded-3xl border bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-md">
      <div className="h-44 bg-gradient-to-br from-orange-100 to-green-100">
        <div className="flex h-full items-center justify-center text-slate-500">
          <Tent className="h-14 w-14" />
        </div>
      </div>

      <div className="p-5">
        <h2 className="text-xl font-bold text-slate-900">
          {campground.name}
        </h2>

        <div className="mt-2 flex items-center gap-2 text-sm text-slate-600">
          <MapPin className="h-4 w-4 text-orange-600" />
          <span>
            {campground.city || "Ville non spécifiée"}
            {campground.postalCode ? ` • ${campground.postalCode}` : ""}
          </span>
        </div>

        <p className="mt-4 line-clamp-3 text-sm text-slate-600">
          {campground.shortDescription ||
            "Découvrez ce camping et ses emplacements disponibles."}
        </p>

        <div className="mt-5 grid grid-cols-2 gap-3 text-sm">
          <InfoPill
            icon={<Tent className="h-4 w-4" />}
            label={`${totalSites} sites`}
          />

          <InfoPill
            icon={<CalendarDays className="h-4 w-4" />}
            label={`${travelerSitesCount} voyageurs`}
          />

          <InfoPill
            icon={<Wifi className="h-4 w-4" />}
            label={campground.hasWifi ? "Wi-Fi" : "Wi-Fi non indiqué"}
          />

          <InfoPill
            icon={<Tent className="h-4 w-4" />}
            label={campground.isWinterCamping ? "Hiver" : "Saisonnier"}
          />
        </div>

        <div className="mt-6 flex flex-wrap gap-3">
          <Link
            to={`/campings/${campground.id}`}
            className="rounded-xl border px-4 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-50"
          >
            Voir le camping
          </Link>

          <Link
            to="/reservation-test"
            className="rounded-xl bg-orange-600 px-4 py-2 text-sm font-semibold text-white hover:bg-orange-700"
          >
            Réserver
          </Link>
        </div>
      </div>
    </div>
  );
}

function InfoPill({ icon, label }) {
  return (
    <div className="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 text-slate-700">
      {icon}
      <span>{label}</span>
    </div>
  );
}
