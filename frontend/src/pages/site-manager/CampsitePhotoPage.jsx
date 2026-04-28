// ============================================================
// Fichier : CampsitePhotoPage.jsx
// Chemin : frontend/src/pages/site-manager
// Dernière modification : 2026-04-18
//
// Résumé :
// - Page dédiée à la gestion des photos d’un site
// ============================================================

import React from "react";
import { useParams } from "react-router-dom";
import CampsitePhotoManager from "../../components/campsite/CampsitePhotoManager";

export default function CampsitePhotoPage() {
  const { siteId } = useParams();

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-4xl mx-auto px-6 py-10">
        <div className="bg-white rounded-3xl shadow-sm border p-6">
          <CampsitePhotoManager campsiteId={siteId} />
        </div>
      </div>
    </div>
  );
}