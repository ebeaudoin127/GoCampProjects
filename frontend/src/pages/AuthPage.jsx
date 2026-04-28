// ============================================================
// Fichier : src/pages/AuthPage.jsx
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé des modifications :
// - Redirection dynamique selon le rôle après login/register
// ============================================================

import React, { useState, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";

export default function AuthPage() {
  const [mode, setMode] = useState("login");
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const [countries, setCountries] = useState([]);
  const [provinces, setProvinces] = useState([]);

  const navigate = useNavigate();
  const { login, loginWithCredentials, setUser, getDefaultRouteForRole } = useAuth();

  useEffect(() => {
    if (mode === "register") {
      api
        .get("/countries")
        .then((data) => setCountries(data))
        .catch(() => setError("Impossible de charger la liste des pays."));
    }
  }, [mode]);

  const handleCountryChange = async (countryId) => {
    setProvinces([]);

    if (!countryId) return;

    try {
      const res = await api.get(`/province-states/by-country/${countryId}`);
      setProvinces(res);
    } catch (err) {
      console.error("Erreur chargement provinces:", err);
      setError("Impossible de charger les provinces.");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    setError("");

    const form = new FormData(e.target);

    const firstname = form.get("firstname");
    const lastname = form.get("lastname");
    const email = form.get("email");
    const password = form.get("password");
    const confirm = form.get("confirm");

    const countryId = form.get("countryId");
    const provinceStateId = form.get("provinceStateId");

    if (!email || !password) {
      setError("Veuillez remplir tous les champs obligatoires.");
      setSubmitting(false);
      return;
    }

    if (mode === "register") {
      if (!firstname || !lastname) {
        setError("Veuillez entrer votre prénom et nom.");
        setSubmitting(false);
        return;
      }

      if (password !== confirm) {
        setError("Les mots de passe ne correspondent pas.");
        setSubmitting(false);
        return;
      }

      if (!countryId || !provinceStateId) {
        setError("Veuillez sélectionner un pays et une province.");
        setSubmitting(false);
        return;
      }
    }

    try {
      if (mode === "login") {
        await loginWithCredentials(email, password);

        const me = await api.get("/auth/me");
        setUser(me);

        navigate(getDefaultRouteForRole(me), { replace: true });
        return;
      }

      const payload = {
        firstname,
        lastname,
        email,
        password,
        countryId: Number(countryId),
        provinceStateId: Number(provinceStateId),
      };

      const data = await api.post("/auth/register", payload);

      if (!data.token) {
        throw new Error("Le serveur n'a renvoyé aucun token.");
      }

      login(data.token);

      const me = await api.get("/auth/me");
      setUser(me);

      navigate(getDefaultRouteForRole(me), { replace: true });
    } catch (err) {
      console.error("Erreur Auth:", err);
      setError(err.message ?? "Erreur inconnue.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-orange-50 px-6 py-12">
      <div className="max-w-5xl w-full bg-white rounded-2xl shadow-xl grid md:grid-cols-2 overflow-hidden">
        <motion.div
          initial={{ opacity: 0, x: -40 }}
          animate={{ opacity: 1, x: 0 }}
          className="p-10"
        >
          <h1 className="text-3xl font-bold">
            {mode === "login" ? "Connexion" : "Créer un compte"}
          </h1>

          <form onSubmit={handleSubmit} className="mt-8 space-y-4">
            <AnimatePresence mode="wait">
              {mode === "register" && (
                <motion.div
                  key="register-fields"
                  initial={{ opacity: 0, y: -10 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: 10 }}
                  className="space-y-4"
                >
                  <input
                    name="firstname"
                    placeholder="Prénom"
                    className="w-full border rounded-lg px-4 py-2"
                  />
                  <input
                    name="lastname"
                    placeholder="Nom"
                    className="w-full border rounded-lg px-4 py-2"
                  />

                  <select
                    name="countryId"
                    className="w-full border rounded-lg px-4 py-2"
                    onChange={(e) => handleCountryChange(e.target.value)}
                  >
                    <option value="">Choisir un pays</option>
                    {countries.map((c) => (
                      <option key={c.id} value={c.id}>
                        {c.name}
                      </option>
                    ))}
                  </select>

                  <select
                    name="provinceStateId"
                    className="w-full border rounded-lg px-4 py-2"
                  >
                    <option value="">Choisir une province</option>
                    {provinces.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.name}
                      </option>
                    ))}
                  </select>
                </motion.div>
              )}
            </AnimatePresence>

            <input
              name="email"
              type="email"
              placeholder="Adresse courriel"
              className="w-full border rounded-lg px-4 py-2"
            />
            <input
              name="password"
              type="password"
              placeholder="Mot de passe"
              className="w-full border rounded-lg px-4 py-2"
            />

            {mode === "register" && (
              <input
                name="confirm"
                type="password"
                placeholder="Confirmer le mot de passe"
                className="w-full border rounded-lg px-4 py-2"
              />
            )}

            {error && <p className="text-red-600 text-sm">{error}</p>}

            <button
              className="w-full bg-orange-600 text-white py-3 rounded-lg font-semibold hover:bg-orange-700 disabled:opacity-50"
              disabled={submitting}
            >
              {submitting
                ? "Veuillez patienter..."
                : mode === "login"
                ? "Se connecter"
                : "Créer mon compte"}
            </button>

            <p className="text-center text-sm mt-4">
              {mode === "login" ? (
                <>
                  Pas de compte ?{" "}
                  <button
                    type="button"
                    className="text-orange-600"
                    onClick={() => setMode("register")}
                  >
                    Créer un compte
                  </button>
                </>
              ) : (
                <>
                  Déjà un compte ?{" "}
                  <button
                    type="button"
                    className="text-orange-600"
                    onClick={() => setMode("login")}
                  >
                    Se connecter
                  </button>
                </>
              )}
            </p>
          </form>
        </motion.div>

        <motion.div
          initial={{ opacity: 0, x: 40 }}
          animate={{ opacity: 1, x: 0 }}
          className="hidden md:block"
        >
          <img
            src="/backgrounds/hero.png"
            alt="Camping"
            className="w-full h-full object-cover"
          />
        </motion.div>
      </div>
    </div>
  );
}