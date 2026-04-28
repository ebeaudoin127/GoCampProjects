// ============================================================
// Fichier : src/pages/admin/CampingManagerHome.jsx
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Tableau de bord de base pour CAMPING_ADMIN / GESTIONNAIRE
// ============================================================

import React from "react";
import { Building2, MapPinned, CalendarCheck2, Users } from "lucide-react";
import { useAuth } from "../../context/AuthContext";

export default function CampingManagerHome() {
  const { user, getRoleName } = useAuth();
  const roleName = getRoleName(user);

  const modules = [
    {
      title: "Mes campings",
      description:
        "Accéder à la liste des campings que vous êtes autorisé à administrer.",
      icon: Building2,
    },
    {
      title: "Emplacements",
      description:
        "Configurer les sites, les dimensions, les services et l’occupation.",
      icon: MapPinned,
    },
    {
      title: "Réservations",
      description:
        "Consulter et gérer les réservations liées à vos campings.",
      icon: CalendarCheck2,
    },
    {
      title: "Gestionnaires",
      description:
        "Attribuer les droits permis aux gestionnaires de vos campings.",
      icon: Users,
    },
  ];

  return (
    <div className="min-h-screen bg-emerald-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="bg-white rounded-3xl shadow-sm border p-8 mb-8">
          <h1 className="text-3xl font-bold text-slate-900">
            Gestion camping
          </h1>
          <p className="text-slate-600 mt-2">
            Bienvenue {user?.firstname || "Utilisateur"} — rôle : {roleName || "N/A"}
          </p>
          <p className="text-slate-600 mt-2">
            Cet espace vous permet d’administrer votre ou vos campings selon les
            droits accordés.
          </p>
        </div>

        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          {modules.map((module) => {
            const Icon = module.icon;

            return (
              <div
                key={module.title}
                className="bg-white rounded-3xl shadow-sm border p-6"
              >
                <div className="inline-flex rounded-2xl bg-emerald-100 p-3 text-emerald-700 mb-4">
                  <Icon className="w-6 h-6" />
                </div>

                <h2 className="text-lg font-semibold text-slate-900">
                  {module.title}
                </h2>

                <p className="text-sm text-slate-600 mt-2 leading-6">
                  {module.description}
                </p>

                <button
                  type="button"
                  className="mt-6 rounded-xl bg-emerald-600 px-4 py-2 text-sm font-medium text-white hover:bg-emerald-700"
                >
                  Ouvrir
                </button>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}