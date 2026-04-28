import React from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Footer() {
  const navigate = useNavigate();

  return (
    <footer className="bg-gray-900 text-gray-300 py-12 mt-20">
      <div className="max-w-7xl mx-auto px-6 grid md:grid-cols-4 gap-10">

        {/* Logo */}
        <div>
          <img
            src="/logos/gocamp-logo.png"
            alt="GoCamp Logo"
            className="h-16 mb-4 cursor-pointer"
            onClick={() => navigate("/")}
          />
          <p className="text-sm">
            Explorez facilement des campings partout au Canada et aux États-Unis.
          </p>
        </div>

        {/* Navigation */}
        <div>
          <h3 className="text-lg font-semibold text-white mb-3">Navigation</h3>
          <ul className="space-y-2">
            <li><Link to="/" className="hover:text-white">Accueil</Link></li>
            <li><Link to="/" className="hover:text-white">Campings</Link></li>
            <li><Link to="/" className="hover:text-white">Activités</Link></li>
            <li><Link to="/" className="hover:text-white">À propos</Link></li>
          </ul>
        </div>

        {/* Informations */}
        <div>
          <h3 className="text-lg font-semibold text-white mb-3">Information</h3>
          <ul className="space-y-2">
            <li><Link to="/" className="hover:text-white">Aide</Link></li>
            <li><Link to="/" className="hover:text-white">Contact</Link></li>
            <li><Link to="/" className="hover:text-white">Politique de confidentialité</Link></li>
            <li><Link to="/" className="hover:text-white">Conditions d’utilisation</Link></li>
          </ul>
        </div>

        {/* Réseaux sociaux */}
        <div>
          <h3 className="text-lg font-semibold text-white mb-3">Suivez-nous</h3>
          <div className="flex gap-4 text-gray-300">

            {/* Facebook */}
            <a href="#" className="hover:text-white transition">
              <svg viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                <path d="M22.675 0h-21.35C.595 0 0 .592 0 1.324v21.352C0 23.407.595 24 1.325 24H12.82v-9.294H9.692V11.01h3.127V8.408c0-3.1 1.893-4.788 4.66-4.788 1.324 0 2.463.099 2.796.143v3.24h-1.92c-1.506 0-1.797.716-1.797 1.766v2.316h3.59l-.467 3.696h-3.123V24h6.136C23.405 24 24 23.407 24 22.676V1.324C24 .592 23.405 0 22.675 0z" />
              </svg>
            </a>

            {/* Twitter / X */}
            <a href="#" className="hover:text-white transition">
              <svg viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                <path d="M18.244 2.25h3.308l-7.227 8.26 8.49 11.24h-6.637l-4.356-5.69-5.012 5.69H1.5l7.73-8.76L1.5 2.25h6.797l3.987 5.347 3.96-5.347z" />
              </svg>
            </a>

            {/* Instagram */}
            <a href="#" className="hover:text-white transition">
              <svg viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                <path d="M12 2.163c3.204 0 3.584.012 4.85.07 1.366.062 2.633.334 3.608 1.31.975.975 1.248 2.242 1.31 3.608.058 1.266.07 1.646.07 4.85s-.012 3.584-.07 4.85c-.062 1.366-.334 2.633-1.31 3.608-.975.975-2.242 1.248-3.608 1.31-1.266.058-1.646.07-4.85.07s-3.584-.012-4.85-.07c-1.366-.062-2.633-.334-3.608-1.31-.975-.975-1.248-2.242-1.31-3.608C2.175 15.584 2.163 15.204 2.163 12s.012-3.584.07-4.85c.062-1.366.334-2.633 1.31-3.608.975-.975 2.242-1.248 3.608-1.31C8.416 2.175 8.796 2.163 12 2.163m0 1.837c-3.17 0-3.548.012-4.795.07-1.02.047-1.577.218-1.948.389a3.27 3.27 0 0 0-1.18.77 3.27 3.27 0 0 0-.77 1.18c-.171.371-.342.928-.389 1.948-.058 1.247-.07 1.625-.07 4.795s.012 3.548.07 4.795c.047 1.02.218 1.577.389 1.948.187.434.436.827.77 1.18.353.353.746.583 1.18.77.371.171.928.342 1.948.389 1.247.058 1.625.07 4.795.07s3.548-.012 4.795-.07c1.02-.047 1.577-.218 1.948-.389.434-.187.827-.436 1.18-.77.353-.353.583-.746 1.18-1.18.171-.371.342-.928.389-1.948.058-1.247.07-1.625.07-4.795s-.012-3.548-.07-4.795c-.047-1.02-.218-1.577-.389-1.948a3.27 3.27 0 0 0-.77-1.18 3.27 3.27 0 0 0-1.18-.77c-.371-.171-.928-.342-1.948-.389-1.247-.058-1.625-.07-4.795-.07zm0 3.89a5.953 5.953 0 1 1 0 11.905 5.953 5.953 0 0 1 0-11.905zm6.406-1.843a1.391 1.391 0 1 1 0 2.783 1.391 1.391 0 0 1 0-2.783z" />
              </svg>
            </a>

          </div>
        </div>

      </div>

      {/* Bas du footer */}
      <div className="mt-10 text-center text-sm text-gray-500">
        © {new Date().getFullYear()} GoCamp – Tous droits réservés.
      </div>
    </footer>
  );
}
