// ============================================================
// Fichier : src/pages/admin/SiteManagerHome.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Tableau de bord de base pour SUPER_ADMIN
// - Carte Gestion des campings maintenant branchée
// ============================================================

import React from "react";
import { ShieldCheck, Users, Building2, Settings, BadgeCheck } from "lucide-react";
import { Link } from "react-router-dom";

export default function SiteManagerHome() {
  const modules = [
    {
      title: "Gestion des utilisateurs",
      description:
        "Créer, consulter et modifier les rôles des utilisateurs de la plateforme.",
      icon: Users,
      to: "/site-manager/users",
    },
    {
      title: "Gestion des campings",
      description:
        "Consulter, créer et administrer tous les campings enregistrés sur le site.",
      icon: Building2,
      to: "/site-manager/campgrounds",
    },
    {
      title: "Gestion des rôles",
      description:
        "Attribuer les droits SUPER_ADMIN, CAMPING_ADMIN et les rôles futurs.",
      icon: BadgeCheck,
      to: "/site-manager",
    },
    {
      title: "Paramètres du site",
      description:
        "Configurer les paramètres généraux de la plateforme GoCamp.",
      icon: Settings,
      to: "/site-manager",
    },
  ];

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="bg-white rounded-3xl shadow-sm border p-8 mb-8">
          <div className="flex items-center gap-4">
            <div className="bg-blue-100 text-blue-700 rounded-2xl p-3">
              <ShieldCheck className="w-8 h-8" />
            </div>
            <div>
              <h1 className="text-3xl font-bold text-slate-900">
                Gestionnaire du site
              </h1>
              <p className="text-slate-600 mt-2">
                Espace réservé au SUPER_ADMIN pour administrer l’ensemble de la
                plateforme.
              </p>
            </div>
          </div>
        </div>

        <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          {modules.map((module) => {
            const Icon = module.icon;

            return (
              <div
                key={module.title}
                className="bg-white rounded-3xl shadow-sm border p-6"
              >
                <div className="inline-flex rounded-2xl bg-slate-100 p-3 text-slate-700 mb-4">
                  <Icon className="w-6 h-6" />
                </div>

                <h2 className="text-lg font-semibold text-slate-900">
                  {module.title}
                </h2>

                <p className="text-sm text-slate-600 mt-2 leading-6">
                  {module.description}
                </p>

                <Link
                  to={module.to}
                  className="inline-block mt-6 rounded-xl bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
                >
                  Ouvrir
                </Link>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}