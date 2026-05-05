// ============================================================
// Fichier : frontend/src/components/pricing/PromotionHelpPanel.jsx
// Dernière modification : 2026-05-04
// Auteur : ChatGPT
//
// Résumé :
// - Panneau d’aide contextuelle pour les promotions dynamiques
// - Affiche une explication courte selon le type sélectionné
// - Affiche une prévisualisation lisible de la promotion
// - Permet d’ouvrir une aide détaillée en modal
// ============================================================

import React from "react";
import { HelpCircle, Info } from "lucide-react";

const promotionHelp = {
  PERCENT_DISCOUNT: {
    title: "Rabais en %",
    description: "Réduit le prix selon un pourcentage.",
    example: "Exemple : 20% de rabais sur tout le camping du 1er au 30 juin.",
    required: "Champ requis : Rabais (%)",
  },
  AMOUNT_DISCOUNT: {
    title: "Rabais en $",
    description: "Réduit le total d’un montant fixe.",
    example: "Exemple : 50$ de rabais sur un séjour de 3 nuits et plus.",
    required: "Champ requis : Rabais ($)",
  },
  FIXED_PRICE: {
    title: "Prix fixe",
    description: "Force un prix fixe pour la période ciblée.",
    example: "Exemple : Sites sélectionnés à 49$/nuit en basse saison.",
    required: "Champ requis : Prix fixe ($)",
  },
  BUY_X_PAY_Y: {
    title: "X nuits pour le prix de Y",
    description: "Permet d’offrir des nuits gratuites selon la durée du séjour.",
    example: "Exemple : 3 nuits pour le prix de 2.",
    required: "Champs requis : Nuits achetées + Nuits payées",
  },
  X_NIGHTS_FOR_AMOUNT: {
    title: "X nuits pour X montant",
    description: "Crée un forfait avec un nombre de nuits et un prix total.",
    example: "Exemple : 4 nuits pour 299$.",
    required: "Champs requis : Nombre de nuits + Montant du forfait",
  },
  CONSECUTIVE_WEEKENDS: {
    title: "Fins de semaine consécutives",
    description: "Crée une promotion basée sur plusieurs week-ends consécutifs.",
    example: "Exemple : 4 fins de semaine consécutives pour 600$.",
    required: "Champs requis : Nombre de fins de semaine + Montant total",
  },
  PACKAGE: {
    title: "Forfait",
    description: "Promotion flexible pour des offres groupées.",
    example: "Exemple : Forfait famille incluant 2 nuits + bois + activité.",
    required: "Champ requis : Montant du forfait",
  },
  OTHER: {
    title: "Autre",
    description: "Promotion informative ou spéciale non standardisée.",
    example: "Exemple : Départ tardif gratuit ou bois inclus.",
    required: "Aucun champ financier obligatoire.",
  },
};

function getTargetLabel(targetType) {
  if (targetType === "ALL_CAMPGROUND") return "tout le camping";
  if (targetType === "GROUP") return "un regroupement tarifaire";
  if (targetType === "SITE") return "un site précis";
  if (targetType === "MULTI_CAMPSITE") return "plusieurs sites précis";
  return "une cible non définie";
}

function buildPreview(form) {
  const name = form.name?.trim() || "Promotion sans nom";
  const target = getTargetLabel(form.targetType);
  const start = form.startDate || "date début non définie";
  const end = form.endDate || "date fin non définie";

  let value = "configuration incomplète";

  if (form.promotionType === "PERCENT_DISCOUNT" && form.discountPercent) {
    value = `${form.discountPercent}% de rabais`;
  }

  if (form.promotionType === "AMOUNT_DISCOUNT" && form.discountAmount) {
    value = `${form.discountAmount}$ de rabais`;
  }

  if (form.promotionType === "FIXED_PRICE" && form.fixedPrice) {
    value = `prix fixe de ${form.fixedPrice}$`;
  }

  if (form.promotionType === "BUY_X_PAY_Y" && form.buyNights && form.payNights) {
    value = `${form.buyNights} nuits pour le prix de ${form.payNights}`;
  }

  if (form.promotionType === "X_NIGHTS_FOR_AMOUNT" && form.packageNights && form.packagePrice) {
    value = `${form.packageNights} nuits pour ${form.packagePrice}$`;
  }

  if (
    form.promotionType === "CONSECUTIVE_WEEKENDS" &&
    form.requiredConsecutiveWeekends &&
    form.packagePrice
  ) {
    value = `${form.requiredConsecutiveWeekends} fins de semaine consécutives pour ${form.packagePrice}$`;
  }

  if (form.promotionType === "PACKAGE" && form.packagePrice) {
    value = `forfait à ${form.packagePrice}$`;
  }

  if (form.promotionType === "OTHER") {
    value = "promotion spéciale ou informative";
  }

  const minNights = form.minNights ? ` Minimum ${form.minNights} nuit(s).` : "";
  const maxNights = form.maxNights ? ` Maximum ${form.maxNights} nuit(s).` : "";
  const days =
    Array.isArray(form.daysOfWeek) && form.daysOfWeek.length > 0
      ? ` Jours ciblés : ${form.daysOfWeek.join(", ")}.`
      : " Tous les jours sont admissibles.";

  return `${name} : ${value}, applicable à ${target}, du ${start} au ${end}.${minNights}${maxNights}${days}`;
}

export default function PromotionHelpPanel({ form, onOpenHelp }) {
  const help = promotionHelp[form.promotionType] || promotionHelp.OTHER;
  const preview = buildPreview(form);

  return (
    <div className="space-y-4">
      <div className="rounded-3xl border bg-white p-6 shadow-sm">
        <div className="flex items-start gap-3">
          <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
            <Info className="h-5 w-5" />
          </div>

          <div>
            <h2 className="text-lg font-semibold text-slate-900">
              Aide rapide
            </h2>
            <p className="mt-1 text-sm text-slate-600">
              Cette section t’aide à comprendre le type de promotion sélectionné.
            </p>
          </div>
        </div>

        <div className="mt-5 rounded-2xl bg-slate-50 p-4">
          <p className="text-sm font-semibold text-slate-900">{help.title}</p>
          <p className="mt-1 text-sm text-slate-600">{help.description}</p>
          <p className="mt-3 text-sm text-slate-700">{help.example}</p>
          <p className="mt-3 text-xs font-medium text-slate-500">{help.required}</p>
        </div>

        <button
          type="button"
          onClick={onOpenHelp}
          className="mt-4 inline-flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
        >
          <HelpCircle className="h-4 w-4" />
          Aide détaillée
        </button>
      </div>

      <div className="rounded-3xl border bg-white p-6 shadow-sm">
        <h2 className="text-lg font-semibold text-slate-900">
          Prévisualisation
        </h2>
        <p className="mt-2 text-sm leading-6 text-slate-700">
          {preview}
        </p>

        <div className="mt-4 rounded-2xl border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800">
          Cette promotion est configurée ici, mais elle ne sera appliquée automatiquement au calcul du prix que lorsque le moteur pricing sera branché.
        </div>
      </div>

      <div className="rounded-3xl border bg-white p-6 shadow-sm">
        <h2 className="text-lg font-semibold text-slate-900">
          Règles utiles
        </h2>

        <div className="mt-3 space-y-3 text-sm text-slate-600">
          <p>
            <strong>Priorité :</strong> plus le chiffre est petit, plus la promotion est prioritaire.
          </p>
          <p>
            <strong>Cumulable :</strong> permettra plus tard de combiner cette promotion avec d’autres.
          </p>
          <p>
            <strong>Jours applicables :</strong> si aucun jour n’est sélectionné, la promotion est considérée applicable tous les jours.
          </p>
        </div>
      </div>
    </div>
  );
}
