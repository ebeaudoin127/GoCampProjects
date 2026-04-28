// ============================================================
// Fichier : src/components/icons/YurtIcon.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Icône custom pour yourte
// ============================================================

import React from "react";

export default function YurtIcon({ className = "w-4 h-4" }) {
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
      <path d="M4 20c0-5 3.5-9 8-9s8 4 8 9" />
      <path d="M7 11l5-7 5 7" />
      <path d="M10 20v-4h4v4" />
      <line x1="4" y1="20" x2="20" y2="20" />
    </svg>
  );
}