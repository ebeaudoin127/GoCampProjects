// ============================================================
// Fichier : src/components/icons/CabinIcon.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Icône custom pour cabine / chalet / refuge
// ============================================================

import React from "react";

export default function CabinIcon({ className = "w-4 h-4" }) {
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
      <path d="M6 20V11l6-5 6 5v9" />
      <path d="M9 20v-5h6v5" />
      <line x1="7" y1="11" x2="17" y2="11" />
      <line x1="8" y1="8" x2="16" y2="8" />
    </svg>
  );
}