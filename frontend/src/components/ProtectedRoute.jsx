// ============================================================
// Fichier : src/components/ProtectedRoute.jsx
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Protection basée sur l'utilisateur chargé
// - Support de rôles autorisés
// ============================================================

import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function ProtectedRoute({
  children,
  allowedRoles = [],
  redirectTo = "/auth",
}) {
  const { user, loading, getRoleName, getDefaultRouteForRole } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        Chargement...
      </div>
    );
  }

  if (!user) {
    return <Navigate to={redirectTo} replace />;
  }

  if (allowedRoles.length > 0) {
    const currentRole = getRoleName(user);

    if (!allowedRoles.includes(currentRole)) {
      return <Navigate to={getDefaultRouteForRole(user)} replace />;
    }
  }

  return children;
}