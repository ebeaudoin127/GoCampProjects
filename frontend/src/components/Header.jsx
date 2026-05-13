// ============================================================
// Fichier : Header.jsx
// Chemin  : frontend/src/components
// Dernière modification : 2026-05-12
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Header principal GoCamp
// - Ajout du lien vers l’espace admin selon le rôle
// - Intégration temporaire du module réservation
// - Le bouton Campings ouvre la page publique /campings
//
// Historique des modifications :
// 2026-04-16
// - Ajout du lien vers l’espace admin selon le rôle
//
// 2026-05-06
// - Le bouton "Réserver" ouvre maintenant /reservation-test
//
// 2026-05-12
// - Le bouton "Campings" ouvre maintenant /campings
// ============================================================

import React, { useState } from "react";
import { Menu, X } from "lucide-react";
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
    <header className="bg-white border-b border-gray-100">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-5">
        <button
          type="button"
          onClick={() => navigate("/")}
          className="flex items-center"
        >
          <img
            src="/logos/gocamp-logo.png"
            alt="GoCamp Logo"
            className="h-20 w-auto"
          />
        </button>

        <nav className="hidden items-center gap-8 md:flex">
          <Link
            to="/reservation-test"
            className="font-semibold text-gray-800 hover:text-orange-600"
          >
            Réserver
          </Link>

          <Link
            to="/campings"
            className="font-semibold text-gray-800 hover:text-orange-600"
          >
            Campings
          </Link>

          <Link
            to="/about"
            className="font-semibold text-gray-800 hover:text-orange-600"
          >
            À propos
          </Link>

          <Link
            to="/contact"
            className="font-semibold text-gray-800 hover:text-orange-600"
          >
            Contact
          </Link>

          {isLoggedIn && (
            <Link
              to="/account"
              className="font-semibold text-green-700 hover:text-green-800"
            >
              Mon compte
            </Link>
          )}

          {isLoggedIn && adminLink && (
            <Link
              to={adminLink}
              className="font-semibold text-blue-700 hover:text-blue-800"
            >
              {adminLabel}
            </Link>
          )}
        </nav>

        <div className="hidden items-center gap-4 md:flex">
          {loading ? (
            <span className="text-sm text-gray-500">Chargement...</span>
          ) : isLoggedIn ? (
            <>
              <div className="text-right">
                <div className="text-sm font-semibold text-gray-800">
                  Bonjour, {user?.firstname || "Utilisateur"}
                </div>

                <div className="text-xs uppercase text-gray-500">
                  {safeRole}
                </div>
              </div>

              <button
                type="button"
                onClick={handleLogout}
                className="font-semibold text-red-600 hover:text-red-700"
              >
                Déconnexion
              </button>
            </>
          ) : (
            <Link
              to="/auth"
              className="rounded-xl bg-orange-600 px-4 py-2 font-semibold text-white hover:bg-orange-700"
            >
              Se connecter / Créer un compte
            </Link>
          )}
        </div>

        <button
          type="button"
          onClick={() => setOpen(!open)}
          className="md:hidden"
        >
          {open ? <X /> : <Menu />}
        </button>
      </div>

      {open && (
        <div className="border-t bg-white px-6 py-4 md:hidden">
          <div className="flex flex-col gap-4">
            <Link
              to="/reservation-test"
              onClick={() => setOpen(false)}
              className="font-semibold text-gray-800"
            >
              Réserver
            </Link>

            <Link
              to="/campings"
              onClick={() => setOpen(false)}
              className="font-semibold text-gray-800"
            >
              Campings
            </Link>

            <Link
              to="/about"
              onClick={() => setOpen(false)}
              className="font-semibold text-gray-800"
            >
              À propos
            </Link>

            <Link
              to="/contact"
              onClick={() => setOpen(false)}
              className="font-semibold text-gray-800"
            >
              Contact
            </Link>

            {isLoggedIn && (
              <Link
                to="/account"
                onClick={() => setOpen(false)}
                className="font-semibold text-green-700"
              >
                Mon compte
              </Link>
            )}

            {isLoggedIn && adminLink && (
              <Link
                to={adminLink}
                onClick={() => setOpen(false)}
                className="font-semibold text-blue-700"
              >
                {adminLabel}
              </Link>
            )}

            {loading ? (
              <span className="text-sm text-gray-500">Chargement...</span>
            ) : isLoggedIn ? (
              <button
                type="button"
                onClick={() => {
                  setOpen(false);
                  handleLogout();
                }}
                className="text-left font-semibold text-red-600"
              >
                Déconnexion
              </button>
            ) : (
              <Link
                to="/auth"
                onClick={() => setOpen(false)}
                className="font-semibold text-orange-600"
              >
                Se connecter / Créer un compte
              </Link>
            )}
          </div>
        </div>
      )}
    </header>
  );
}