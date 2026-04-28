// ============================================================
// Fichier : src/context/AuthContext.jsx
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé des modifications :
// - Ajout de getRoleName()
// - Ajout des helpers de rôle
// - Ajout de getDefaultRouteForRole()
// - Centralisation de la logique d’authentification
// ============================================================

import React, { createContext, useContext, useState, useEffect, useMemo } from "react";
import api from "../services/api";

async function safeJson(response) {
  const text = await response.text();
  try {
    return text ? JSON.parse(text) : null;
  } catch (e) {
    console.warn("safeJson parse error:", e);
    return null;
  }
}

export const AuthContext = createContext(null);

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem("token"));
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  function getRoleName(targetUser) {
    if (!targetUser?.role) return null;

    if (typeof targetUser.role === "string") {
      return targetUser.role;
    }

    return targetUser.role.name || targetUser.role.code || null;
  }

  function getDefaultRouteForRole(targetUser) {
    const roleName = getRoleName(targetUser);

    if (roleName === "SUPER_ADMIN") {
      return "/site-manager";
    }

    if (roleName === "CAMPING_ADMIN" || roleName === "GESTIONNAIRE") {
      return "/camping-manager";
    }

    return "/";
  }

  useEffect(() => {
    async function loadUser() {
      if (!token) {
        setUser(null);
        setLoading(false);
        return;
      }

      setLoading(true);

      try {
        const res = await fetch("http://localhost:8080/api/auth/me", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          throw new Error("Token invalide");
        }

        const data = await safeJson(res);
        setUser(data);
      } catch (err) {
        console.error("Auth error:", err);
        localStorage.removeItem("token");
        setToken(null);
        setUser(null);
      } finally {
        setLoading(false);
      }
    }

    loadUser();
  }, [token]);

  async function loginWithCredentials(email, password) {
    const result = await api.login(email, password);

    if (!result?.token) {
      throw new Error("Réponse invalide du serveur (token manquant)");
    }

    localStorage.setItem("token", result.token);
    setToken(result.token);
  }

  function login(newToken) {
    localStorage.setItem("token", newToken);
    setToken(newToken);
  }

  function logout() {
    localStorage.removeItem("token");
    setToken(null);
    setUser(null);
  }

  const roleName = getRoleName(user);

  const value = useMemo(() => {
    return {
      token,
      user,
      loading,
      login,
      loginWithCredentials,
      logout,
      setUser,
      getRoleName,
      getDefaultRouteForRole,
      roleName,
      isAuthenticated: !!token && !!user,
      isSuperAdmin: roleName === "SUPER_ADMIN",
      isCampingAdmin: roleName === "CAMPING_ADMIN",
      isGestionnaire: roleName === "GESTIONNAIRE",
      isUtilisateur: roleName === "UTILISATEUR",
      canAccessSiteManager: roleName === "SUPER_ADMIN",
      canAccessCampingManager:
        roleName === "SUPER_ADMIN" ||
        roleName === "CAMPING_ADMIN" ||
        roleName === "GESTIONNAIRE",
    };
  }, [token, user, loading, roleName]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export default AuthContext;