// ============================================================
// Fichier : src/components/icons/RvIcon.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Icône custom pour véhicule récréatif
// ============================================================

import React from "react";

export default function RvIcon({ className = "w-4 h-4" }) {
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
      <rect x="3" y="7" width="14" height="8" rx="1.5" />
      <path d="M17 9h2.5l2 2v4H17" />
      <line x1="7" y1="7" x2="7" y2="4" />
      <circle cx="7" cy="17" r="1.7" />
      <circle cx="18" cy="17" r="1.7" />
      <line x1="5" y1="15" x2="16" y2="15" />
      <line x1="9" y1="10" x2="13" y2="10" />
    </svg>
  );
}