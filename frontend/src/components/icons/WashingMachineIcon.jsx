// ============================================================
// Fichier : src/components/icons/WashingMachineIcon.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Icône custom pour buanderie / machine à laver
// ============================================================

import React from "react";

export default function WashingMachineIcon({ className = "w-4 h-4" }) {
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
      <rect x="4" y="3" width="16" height="18" rx="2" />
      <line x1="4" y1="9" x2="20" y2="9" />
      <circle cx="12" cy="14" r="4.5" />
      <circle cx="8" cy="6" r="0.7" fill="currentColor" stroke="none" />
      <circle cx="11" cy="6" r="0.7" fill="currentColor" stroke="none" />
    </svg>
  );
}