// ============================================================
// Fichier : frontend/src/components/pricing/PromotionHelpModal.jsx
// Dernière modification : 2026-05-06
// Auteur : ChatGPT
//
// Résumé :
// - Modal d’aide détaillée pour la création de promotions dynamiques
// - Explique tous les types de promotions disponibles
// - Donne les champs requis, exemples et bonnes pratiques
// - Ajout de la cible terrains disponibles / non réservés
// - Ajout des explications sur les codes promo
// ============================================================

import React from "react";
import { X } from "lucide-react";

const sections = [
  {
    title: "Rabais en %",
    type: "PERCENT_DISCOUNT",
    description: "Réduit le prix selon un pourcentage.",
    example: "20% de rabais sur tous les sites du 1er au 30 juin.",
    fields: ["Rabais (%)", "Date début", "Date fin", "Cible"],
    warning: "Évite de dépasser 50% sauf pour une stratégie très ciblée.",
  },
  {
    title: "Rabais en $",
    type: "AMOUNT_DISCOUNT",
    description: "Réduit le total d’un montant fixe.",
    example: "50$ de rabais sur un séjour de 3 nuits et plus.",
    fields: ["Rabais ($)", "Minimum de nuits", "Date début", "Date fin"],
    warning: "Plus tard, il faudra éviter que le rabais dépasse le total de la réservation.",
  },
  {
    title: "Prix fixe",
    type: "FIXED_PRICE",
    description: "Force un prix fixe pour la période ciblée.",
    example: "Sites A101, A102 et A103 à 49$/nuit en basse saison.",
    fields: ["Prix fixe ($)", "Cible", "Date début", "Date fin"],
    warning: "Très utile pour remplir des sites moins populaires.",
  },
  {
    title: "X nuits pour le prix de Y",
    type: "BUY_X_PAY_Y",
    description: "Permet d’offrir une ou plusieurs nuits gratuites.",
    example: "3 nuits pour le prix de 2.",
    fields: ["Nuits achetées", "Nuits payées", "Minimum de nuits"],
    warning: "Les nuits achetées doivent être plus grandes que les nuits payées.",
  },
  {
    title: "X nuits pour X montant",
    type: "X_NIGHTS_FOR_AMOUNT",
    description: "Crée un forfait basé sur un nombre de nuits et un montant total.",
    example: "4 nuits pour 299$.",
    fields: ["Nombre de nuits", "Montant du forfait ($)", "Cible"],
    warning: "Très utile pour les longs week-ends ou les périodes creuses.",
  },
  {
    title: "Fins de semaine consécutives",
    type: "CONSECUTIVE_WEEKENDS",
    description: "Crée une offre pour des clients qui réservent plusieurs week-ends de suite.",
    example: "4 fins de semaine consécutives pour 600$.",
    fields: ["Nombre de fins de semaine consécutives", "Montant total ($)", "Dates"],
    warning: "La validation exacte sera faite plus tard dans le moteur de réservation/pricing.",
  },
  {
    title: "Forfait",
    type: "PACKAGE",
    description: "Permet de préparer des offres groupées plus flexibles.",
    example: "Forfait famille : 2 nuits + bois + activité pour 249$.",
    fields: ["Montant du forfait", "Description", "Conditions"],
    warning: "Idéal pour des offres marketing plus riches.",
  },
  {
    title: "Autre",
    type: "OTHER",
    description: "Promotion spéciale ou informative qui ne rentre pas dans les autres types.",
    example: "Départ tardif gratuit, bois inclus, rabais sur location d’équipement.",
    fields: ["Nom", "Description", "Conditions"],
    warning: "Ce type est surtout informatif tant que le moteur de calcul n’est pas branché.",
  },
];

export default function PromotionHelpModal({ open, onClose }) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 bg-black/50 px-4 py-6">
      <div className="mx-auto flex max-h-[92vh] max-w-6xl flex-col overflow-hidden rounded-3xl bg-white shadow-2xl">
        <div className="flex items-start justify-between gap-4 border-b px-6 py-5">
          <div>
            <h2 className="text-2xl font-bold text-slate-900">
              Aide détaillée — Promotions dynamiques
            </h2>
            <p className="mt-1 text-sm text-slate-600">
              Cette aide explique comment choisir le bon type de promotion et comment remplir les champs.
            </p>
          </div>

          <button
            type="button"
            onClick={onClose}
            className="rounded-xl border p-2 text-slate-600 hover:bg-slate-100"
            aria-label="Fermer l'aide"
          >
            <X className="h-5 w-5" />
          </button>
        </div>

        <div className="overflow-y-auto px-6 py-6">
          <div className="mb-6 rounded-2xl border border-blue-200 bg-blue-50 p-4 text-sm text-blue-900">
            <p className="font-semibold">Important</p>
            <p className="mt-1">
              Cette page sert à configurer les promotions. Elles ne sont pas encore appliquées automatiquement au prix final tant que le moteur pricing n’est pas branché.
            </p>
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            {sections.map((section) => (
              <div key={section.type} className="rounded-2xl border p-5">
                <div className="flex flex-wrap items-center gap-2">
                  <h3 className="text-lg font-semibold text-slate-900">
                    {section.title}
                  </h3>
                  <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-600">
                    {section.type}
                  </span>
                </div>

                <p className="mt-3 text-sm text-slate-600">
                  {section.description}
                </p>

                <div className="mt-4 rounded-xl bg-slate-50 p-4">
                  <p className="text-sm font-semibold text-slate-800">
                    Exemple
                  </p>
                  <p className="mt-1 text-sm text-slate-600">
                    {section.example}
                  </p>
                </div>

                <div className="mt-4">
                  <p className="text-sm font-semibold text-slate-800">
                    Champs importants
                  </p>
                  <ul className="mt-2 list-disc space-y-1 pl-5 text-sm text-slate-600">
                    {section.fields.map((field) => (
                      <li key={field}>{field}</li>
                    ))}
                  </ul>
                </div>

                <div className="mt-4 rounded-xl border border-amber-200 bg-amber-50 p-3 text-sm text-amber-800">
                  {section.warning}
                </div>
              </div>
            ))}
          </div>

          <div className="mt-6 rounded-2xl border border-emerald-200 bg-emerald-50 p-5">
            <h3 className="text-lg font-semibold text-emerald-950">
              Cible terrains disponibles / non réservés
            </h3>

            <div className="mt-3 space-y-2 text-sm text-emerald-900">
              <p>
                Cette cible sert aux promotions de remplissage. Elle vise les terrains encore libres pour une période donnée.
              </p>
              <p>
                Exemple : jeudi midi, seulement 40% des sites sont loués pour vendredi et samedi.
                Le gestionnaire peut créer une promotion de 25% sur les terrains disponibles pour vendredi soir et samedi soir.
              </p>
              <p>
                Cette cible deviendra pleinement intelligente lorsque le moteur de réservation pourra vérifier précisément quels terrains sont déjà réservés.
              </p>
            </div>
          </div>

          <div className="mt-6 rounded-2xl border border-blue-200 bg-blue-50 p-5">
            <h3 className="text-lg font-semibold text-blue-950">
              Codes promo
            </h3>

            <div className="mt-3 space-y-2 text-sm text-blue-900">
              <p>
                Le code promo permet de contrôler ou d’identifier une campagne.
              </p>
              <p>
                <strong>Code promo obligatoire :</strong> le client doit entrer le code exact pour obtenir le rabais.
                Exemple : ETE2026.
              </p>
              <p>
                <strong>Code promo non obligatoire :</strong> le code peut servir de référence marketing ou administrative,
                mais la promotion peut quand même s’appliquer automatiquement.
              </p>
            </div>
          </div>

          <div className="mt-6 rounded-2xl border bg-slate-50 p-5">
            <h3 className="text-lg font-semibold text-slate-900">
              Recommandations générales
            </h3>

            <div className="mt-3 space-y-2 text-sm text-slate-700">
              <p>
                <strong>Cible tout le camping :</strong> pratique pour une campagne générale.
              </p>
              <p>
                <strong>Cible regroupement :</strong> pratique pour appliquer une offre à une famille de sites.
              </p>
              <p>
                <strong>Cible site unique :</strong> pratique pour remplir un site précis.
              </p>
              <p>
                <strong>Cible multi-sites :</strong> pratique pour sélectionner plusieurs sites sans créer un regroupement tarifaire.
              </p>
              <p>
                <strong>Cible terrains disponibles :</strong> pratique pour remplir les sites libres à court terme.
              </p>
              <p>
                <strong>Priorité :</strong> utilise 10, 20, 30 plutôt que 1, 2, 3 pour garder de l’espace entre les règles.
              </p>
            </div>
          </div>
        </div>

        <div className="border-t px-6 py-4 text-right">
          <button
            type="button"
            onClick={onClose}
            className="rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white hover:bg-slate-800"
          >
            Fermer
          </button>
        </div>
      </div>
    </div>
  );
}