// ============================================================
// Fichier : frontend/src/components/campsite/CampsitePricingOptionHelpModal.jsx
// Dernière modification : 2026-05-05
//
// Résumé :
// - Fenêtre d’aide pour les valeurs de tarification d’un site
// - Explique quand créer, utiliser ou supprimer une valeur
// ============================================================

import React from "react";
import { HelpCircle, X } from "lucide-react";

export default function CampsitePricingOptionHelpModal({ open, onClose }) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-[999] flex items-center justify-center bg-slate-900/50 px-4">
      <div className="w-full max-w-3xl rounded-3xl bg-white shadow-2xl">
        <div className="flex items-start justify-between gap-4 border-b px-6 py-5">
          <div className="flex items-start gap-3">
            <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
              <HelpCircle className="h-6 w-6" />
            </div>

            <div>
              <h2 className="text-xl font-bold text-slate-900">
                Aide — Valeurs de tarification
              </h2>
              <p className="mt-1 text-sm text-slate-600">
                Comprendre comment classer les sites pour appliquer les bons tarifs.
              </p>
            </div>
          </div>

          <button
            type="button"
            onClick={onClose}
            className="rounded-xl border p-2 text-slate-500 hover:bg-slate-50 hover:text-slate-900"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <div className="space-y-5 px-6 py-5 text-sm text-slate-700">
          <section>
            <h3 className="font-semibold text-slate-900">
              C’est quoi une valeur de tarification?
            </h3>
            <p className="mt-1">
              C’est une catégorie tarifaire utilisée pour regrouper des sites qui
              auront les mêmes règles de prix. Par exemple : Standard, Premium,
              Bord de l’eau, Prêt-à-camper, Terrain saisonnier.
            </p>
          </section>

          <section>
            <h3 className="font-semibold text-slate-900">
              Exemple simple
            </h3>
            <div className="mt-2 rounded-2xl bg-slate-50 p-4">
              <p>Site 101 → Standard</p>
              <p>Site 102 → Standard</p>
              <p>Site 201 → Bord de l’eau</p>
              <p>Site 301 → Premium</p>
            </div>
          </section>

          <section>
            <h3 className="font-semibold text-slate-900">
              Quand créer une nouvelle valeur?
            </h3>
            <p className="mt-1">
              Crée une nouvelle valeur si certains sites doivent avoir une logique
              de prix différente. Exemple : les sites au bord de l’eau coûtent plus
              cher que les sites standards.
            </p>
          </section>

          <section>
            <h3 className="font-semibold text-slate-900">
              Quand éviter d’en créer une?
            </h3>
            <p className="mt-1">
              Évite de créer une valeur différente pour chaque site si les prix
              sont identiques. Sinon la gestion tarifaire devient plus lourde.
            </p>
          </section>

          <section>
            <h3 className="font-semibold text-slate-900">
              Suppression
            </h3>
            <p className="mt-1">
              Une valeur peut être supprimée seulement si elle n’est pas utilisée
              par un site, une règle tarifaire ou une promotion. C’est volontaire :
              cela évite de briser l’historique et les calculs de prix.
            </p>
          </section>
        </div>

        <div className="flex justify-end border-t px-6 py-4">
          <button
            type="button"
            onClick={onClose}
            className="rounded-xl bg-slate-900 px-5 py-2.5 text-sm font-medium text-white hover:bg-slate-800"
          >
            Compris
          </button>
        </div>
      </div>
    </div>
  );
}