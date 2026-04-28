// ============================================================
// Fichier : AccountPage.jsx
// Dernière modification : 2026-04-16
// Résumé des modifications :
// - Ajout de l'onglet "Équipement" entre Profil et Sécurité
// - Intégration du nouveau composant EquipementsTab
// - Conservation de la logique existante pour le mot de passe
// ============================================================

import React, { useState } from "react";
import { Navigate } from "react-router-dom";
import { motion } from "framer-motion";
import UserProfileForm from "../components/UserProfileForm";
import EquipementsTab from "../components/EquipementsTab";
import { useAuth } from "../context/AuthContext";
import api from "../services/api";

export default function AccountPage() {
  const { user, loading } = useAuth();

  const [activeTab, setActiveTab] = useState("profile");
  const [securityMessage, setSecurityMessage] = useState("");

  if (!loading && !user) {
    return <Navigate to="/auth" replace />;
  }

  if (loading || !user) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p>Chargement...</p>
      </div>
    );
  }

  const handleChangePassword = async () => {
    setSecurityMessage("");

    const oldPassword = document.querySelector("#oldPassword")?.value.trim();
    const newPassword = document.querySelector("#newPassword")?.value.trim();
    const confirmPassword =
      document.querySelector("#confirmPassword")?.value.trim();

    if (!oldPassword || !newPassword || !confirmPassword) {
      setSecurityMessage("Veuillez remplir tous les champs.");
      return;
    }

    if (newPassword !== confirmPassword) {
      setSecurityMessage("Les mots de passe ne correspondent pas.");
      return;
    }

    try {
      await api.put("/auth/change-password", {
        oldPassword,
        newPassword,
      });

      setSecurityMessage("Mot de passe mis à jour !");

      const oldInput = document.querySelector("#oldPassword");
      const newInput = document.querySelector("#newPassword");
      const confirmInput = document.querySelector("#confirmPassword");

      if (oldInput) oldInput.value = "";
      if (newInput) newInput.value = "";
      if (confirmInput) confirmInput.value = "";
    } catch (err) {
      console.error("Erreur changement mot de passe :", err);
      setSecurityMessage(
        err?.response?.data || "Erreur lors du changement de mot de passe."
      );
    }
  };

  return (
    <div className="min-h-screen bg-orange-50 flex flex-col">
      <div className="flex-1 max-w-5xl mx-auto px-6 py-10">
        <div className="flex gap-6 mb-10 border-b pb-2">
          <button
            onClick={() => setActiveTab("profile")}
            className={`pb-2 text-lg font-semibold ${
              activeTab === "profile"
                ? "text-orange-600 border-b-4 border-orange-600"
                : "text-gray-500"
            }`}
          >
            Profil
          </button>

          <button
            onClick={() => setActiveTab("equipment")}
            className={`pb-2 text-lg font-semibold ${
              activeTab === "equipment"
                ? "text-orange-600 border-b-4 border-orange-600"
                : "text-gray-500"
            }`}
          >
            Équipement
          </button>

          <button
            onClick={() => setActiveTab("security")}
            className={`pb-2 text-lg font-semibold ${
              activeTab === "security"
                ? "text-orange-600 border-b-4 border-orange-600"
                : "text-gray-500"
            }`}
          >
            Sécurité
          </button>
        </div>

        {activeTab === "profile" && (
          <motion.div
            key="profile"
            initial={{ opacity: 0, x: -25 }}
            animate={{ opacity: 1, x: 0 }}
            className="bg-white shadow-xl rounded-2xl p-10"
          >
            <h2 className="text-2xl font-bold mb-6">Informations du profil</h2>
            <UserProfileForm />
          </motion.div>
        )}

        {activeTab === "equipment" && (
          <motion.div
            key="equipment"
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-white shadow-xl rounded-2xl p-10"
          >
            <h2 className="text-2xl font-bold mb-6">Équipement</h2>
            <EquipementsTab />
          </motion.div>
        )}

        {activeTab === "security" && (
          <motion.div
            key="security"
            initial={{ opacity: 0, x: 25 }}
            animate={{ opacity: 1, x: 0 }}
            className="bg-white shadow-xl rounded-2xl p-10"
          >
            <h2 className="text-2xl font-bold mb-6">Sécurité</h2>

            <div className="space-y-6">
              <div>
                <label className="block mb-2 font-medium">
                  Ancien mot de passe
                </label>
                <input
                  id="oldPassword"
                  type="password"
                  className="w-full border rounded-lg px-4 py-2"
                />
              </div>

              <div>
                <label className="block mb-2 font-medium">
                  Nouveau mot de passe
                </label>
                <input
                  id="newPassword"
                  type="password"
                  className="w-full border rounded-lg px-4 py-2"
                />
              </div>

              <div>
                <label className="block mb-2 font-medium">Confirmer</label>
                <input
                  id="confirmPassword"
                  type="password"
                  className="w-full border rounded-lg px-4 py-2"
                />
              </div>

              {securityMessage && (
                <p className="text-orange-600 text-center">{securityMessage}</p>
              )}

              <button
                onClick={handleChangePassword}
                className="w-full bg-orange-600 text-white font-semibold py-3 rounded-lg hover:bg-orange-700"
              >
                Changer le mot de passe
              </button>
            </div>
          </motion.div>
        )}
      </div>
    </div>
  );
}