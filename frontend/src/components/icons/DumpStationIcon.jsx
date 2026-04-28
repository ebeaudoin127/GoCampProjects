// ============================================================
// Fichier : src/components/icons/DumpStationIcon.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Icône custom pour station de vidange
// ============================================================

import React from "react";

export default function DumpStationIcon({ className = "w-4 h-4" }) {
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
      <rect x="4" y="4" width="6" height="10" rx="1" />
      <path d="M10 8h4l3 3" />
      <path d="M17 11v4" />
      <path d="M17 15c0 2-1.5 3.5-3.5 3.5S10 17 10 15" />
      <path d="M7 14v5" />
      <line x1="5" y1="19" x2="9" y2="19" />
    </svg>
  );
}