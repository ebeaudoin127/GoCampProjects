// ============================================================
// Fichier : src/components/Header.jsx
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Ajout du lien vers l’espace admin selon le rôle
// ============================================================

import React, { useState } from "react";
import { Menu, X, User } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export default function Header() {
  const [open, setOpen] = useState(false);
  const {
    user,
    logout,
    token,
    loading,
    getRoleName,
    canAccessSiteManager,
    canAccessCampingManager,
  } = useAuth();

  const navigate = useNavigate();

  const isLoggedIn = !!token && !!user;

  const safeRole = getRoleName(user) || "Compte GoCamp";

  const adminLink = canAccessSiteManager
    ? "/site-manager"
    : canAccessCampingManager
    ? "/camping-manager"
    : null;

  const adminLabel = canAccessSiteManager
    ? "Gestion du site"
    : canAccessCampingManager
    ? "Gestion camping"
    : null;

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <header className="bg-white shadow-sm relative z-30">
      <div className="max-w-7xl mx-auto px-6 py-4 flex items-center justify-between">
        <img
          src="/logos/gocamp-logo.png"
          alt="GoCamp Logo"
          className="h-[150px] w-auto object-contain cursor-pointer"
          onClick={() => navigate("/")}
        />

        <nav className="hidden md:flex items-center gap-8 text-gray-700 font-medium">
          <Link to="/" className="hover:text-green-600">
            Réserver
          </Link>
          <Link to="/" className="hover:text-green-600">
            Campings
          </Link>
          <Link to="/" className="hover:text-green-600">
            À propos
          </Link>
          <Link to="/" className="hover:text-green-600">
            Contact
          </Link>

          {isLoggedIn && (
            <Link to="/account" className="text-green-600 hover:underline">
              Mon compte
            </Link>
          )}

          {isLoggedIn && adminLink && (
            <Link to={adminLink} className="text-blue-600 hover:underline">
              {adminLabel}
            </Link>
          )}

          {loading ? (
            <span>Chargement...</span>
          ) : isLoggedIn ? (
            <div className="flex items-center gap-4">
              <div className="text-right text-sm">
                <div className="font-semibold">
                  Bonjour, {user?.firstname || "Utilisateur"}
                </div>
                <div className="text-gray-500 text-xs">{safeRole}</div>
              </div>
              <button onClick={handleLogout} className="text-red-600 hover:underline">
                Déconnexion
              </button>
            </div>
          ) : (
            <Link to="/auth" className="flex items-center gap-2">
              <User size={18} />
              Se connecter / Créer un compte
            </Link>
          )}
        </nav>

        <button className="md:hidden" onClick={() => setOpen(!open)}>
          {open ? <X size={28} /> : <Menu size={28} />}
        </button>
      </div>

      {open && (
        <nav className="md:hidden bg-white border-t px-6 py-4 flex flex-col gap-4">
          <Link to="/">Réserver</Link>
          <Link to="/">Campings</Link>
          <Link to="/">À propos</Link>
          <Link to="/">Contact</Link>

          {isLoggedIn && <Link to="/account">Mon compte</Link>}

          {isLoggedIn && adminLink && <Link to={adminLink}>{adminLabel}</Link>}

          {loading ? (
            <span>Chargement...</span>
          ) : isLoggedIn ? (
            <button onClick={handleLogout} className="text-red-600 text-left">
              Déconnexion
            </button>
          ) : (
            <Link to="/auth">Se connecter / Créer un compte</Link>
          )}
        </nav>
      )}
    </header>
  );
}