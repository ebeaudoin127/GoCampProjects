// ============================================================
// Fichier : Header.jsx
// Chemin  : frontend/src/components
// Dernière modification : 2026-05-06
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Header principal GoCamp
// - Ajout du lien vers l’espace admin selon le rôle
// - Intégration temporaire du module réservation
//
// Historique des modifications :
// 2026-04-16
// - Ajout du lien vers l’espace admin selon le rôle
//
// 2026-05-06
// - Le bouton "Réserver" ouvre maintenant /reservation-test
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

          {/* RÉSERVER */}
          <Link
            to="/reservation-test"
            className="hover:text-green-600"
          >
            Réserver
          </Link>

          {/* CAMPINGS */}
          <Link
            to="/"
            className="hover:text-green-600"
          >
            Campings
          </Link>

          {/* À PROPOS */}
          <Link
            to="/"
            className="hover:text-green-600"
          >
            À propos
          </Link>

          {/* CONTACT */}
          <Link
            to="/"
            className="hover:text-green-600"
          >
            Contact
          </Link>

          {/* MON COMPTE */}
          {isLoggedIn && (
            <Link
              to="/account"
              className="text-green-600 hover:underline"
            >
              Mon compte
            </Link>
          )}

          {/* ADMIN */}
          {isLoggedIn && adminLink && (
            <Link
              to={adminLink}
              className="text-blue-600 hover:underline"
            >
              {adminLabel}
            </Link>
          )}

          {/* AUTH */}
          {loading ? (

            <span>Chargement...</span>

          ) : isLoggedIn ? (

            <div className="flex items-center gap-4">

              <div className="text-right text-sm">
                <div className="font-semibold">
                  Bonjour, {user?.firstname || "Utilisateur"}
                </div>

                <div className="text-gray-500 text-xs">
                  {safeRole}
                </div>
              </div>

              <button
                onClick={handleLogout}
                className="text-red-600 hover:underline"
              >
                Déconnexion
              </button>
            </div>

          ) : (

            <Link
              to="/auth"
              className="flex items-center gap-2"
            >
              <User size={18} />
              Se connecter / Créer un compte
            </Link>
          )}
        </nav>

        {/* MOBILE MENU BUTTON */}
        <button
          className="md:hidden"
          onClick={() => setOpen(!open)}
        >
          {open ? <X size={28} /> : <Menu size={28} />}
        </button>
      </div>

      {/* MOBILE MENU */}
      {open && (

        <nav className="md:hidden bg-white border-t px-6 py-4 flex flex-col gap-4">

          <Link to="/reservation-test">
            Réserver
          </Link>

          <Link to="/">
            Campings
          </Link>

          <Link to="/">
            À propos
          </Link>

          <Link to="/">
            Contact
          </Link>

          {isLoggedIn && (
            <Link to="/account">
              Mon compte
            </Link>
          )}

          {isLoggedIn && adminLink && (
            <Link to={adminLink}>
              {adminLabel}
            </Link>
          )}

          {loading ? (

            <span>Chargement...</span>

          ) : isLoggedIn ? (

            <button
              onClick={handleLogout}
              className="text-red-600 text-left"
            >
              Déconnexion
            </button>

          ) : (

            <Link to="/auth">
              Se connecter / Créer un compte
            </Link>
          )}
        </nav>
      )}
    </header>
  );
}