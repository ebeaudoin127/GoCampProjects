// ============================================================
// Fichier : src/components/icons/ShowerIcon.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Icône custom pour douche
// ============================================================

import React from "react";

export default function ShowerIcon({ className = "w-4 h-4" }) {
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
      <path d="M7 7a5 5 0 0 1 10 0" />
      <path d="M17 7v3" />
      <path d="M17 10h-6" />
      <path d="M11 10c3 0 6 2 8 5" />
      <line x1="10" y1="16" x2="10" y2="18" />
      <line x1="13" y1="17" x2="13" y2="19" />
      <line x1="16" y1="18" x2="16" y2="20" />
      <line x1="19" y1="17" x2="19" y2="19" />
    </svg>
  );
}