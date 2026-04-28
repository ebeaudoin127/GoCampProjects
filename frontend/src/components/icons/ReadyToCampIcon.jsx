// ============================================================
// Fichier : src/components/icons/ReadyToCampIcon.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Icône custom pour prêt-à-camper
// ============================================================

import React from "react";

export default function ReadyToCampIcon({ className = "w-4 h-4" }) {
  return (
    <svg
      viewBox="0 0 24 24"
      className={className}
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="M4 20h16" />
      <path d="M6 20V10l6-4 6 4v10" />
      <path d="M9 20v-5h6v5" />
      <path d="M8 10h8" />
    </svg>
  );
}