// ============================================================
// Fichier : src/pages/admin/SiteUsersPage.jsx
// Dernière modification : 2026-04-16
// Auteur : ChatGPT
//
// Résumé :
// - Liste des utilisateurs pour le SUPER_ADMIN
// - Permet de changer le rôle d’un utilisateur
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import { ArrowLeft, RefreshCcw, Save, Search, Users } from "lucide-react";
import { Link } from "react-router-dom";
import api from "../../services/api";
import { useAuth } from "../../context/AuthContext";

export default function SiteUsersPage() {
  const { user } = useAuth();

  const [users, setUsers] = useState([]);
  const [roles, setRoles] = useState([]);
  const [draftRoles, setDraftRoles] = useState({});
  const [loading, setLoading] = useState(true);
  const [savingUserId, setSavingUserId] = useState(null);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [search, setSearch] = useState("");

  const loadData = async () => {
    setLoading(true);
    setError("");
    setSuccessMessage("");

    try {
      const [usersData, rolesData] = await Promise.all([
        api.get("/admin/site/users"),
        api.get("/admin/site/roles"),
      ]);

      setUsers(Array.isArray(usersData) ? usersData : []);
      setRoles(Array.isArray(rolesData) ? rolesData : []);

      const initialDrafts = {};
      (Array.isArray(usersData) ? usersData : []).forEach((u) => {
        initialDrafts[u.id] = u.roleId ?? "";
      });
      setDraftRoles(initialDrafts);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger la gestion des utilisateurs.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const filteredUsers = useMemo(() => {
    const q = search.trim().toLowerCase();

    if (!q) return users;

    return users.filter((u) => {
      const haystack = [
        u.firstname,
        u.lastname,
        u.email,
        u.phone,
        u.city,
        u.roleName,
      ]
        .filter(Boolean)
        .join(" ")
        .toLowerCase();

      return haystack.includes(q);
    });
  }, [users, search]);

  const handleRoleChange = (userId, roleId) => {
    setDraftRoles((prev) => ({
      ...prev,
      [userId]: roleId === "" ? "" : Number(roleId),
    }));
  };

  const saveRole = async (targetUser) => {
    const selectedRoleId = draftRoles[targetUser.id];

    if (!selectedRoleId) {
      setError("Veuillez choisir un rôle valide.");
      return;
    }

    setSavingUserId(targetUser.id);
    setError("");
    setSuccessMessage("");

    try {
      const updated = await api.put(`/admin/site/users/${targetUser.id}/role`, {
        roleId: Number(selectedRoleId),
      });

      setUsers((prev) =>
        prev.map((u) =>
          u.id === targetUser.id
            ? {
                ...u,
                roleId: updated.roleId,
                roleName: updated.roleName,
              }
            : u
        )
      );

      setSuccessMessage(
        `Le rôle de ${targetUser.firstname} ${targetUser.lastname} a été mis à jour.`
      );
    } catch (err) {
      console.error(err);
      setError(err.message || "Impossible de modifier le rôle.");
    } finally {
      setSavingUserId(null);
    }
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="max-w-7xl mx-auto px-6 py-10">
        <div className="flex flex-wrap items-center justify-between gap-4 mb-6">
          <div>
            <Link
              to="/site-manager"
              className="inline-flex items-center gap-2 text-sm text-slate-600 hover:text-slate-900"
            >
              <ArrowLeft className="w-4 h-4" />
              Retour au gestionnaire du site
            </Link>

            <div className="flex items-center gap-3 mt-3">
              <div className="rounded-2xl bg-blue-100 p-3 text-blue-700">
                <Users className="w-6 h-6" />
              </div>
              <div>
                <h1 className="text-3xl font-bold text-slate-900">
                  Gestion des utilisateurs
                </h1>
                <p className="text-slate-600 mt-1">
                  Connecté en tant que {user?.firstname || "SUPER_ADMIN"}.
                </p>
              </div>
            </div>
          </div>

          <button
            type="button"
            onClick={loadData}
            className="inline-flex items-center gap-2 rounded-xl bg-white border px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100"
          >
            <RefreshCcw className="w-4 h-4" />
            Actualiser
          </button>
        </div>

        <div className="bg-white rounded-3xl shadow-sm border p-6 mb-6">
          <div className="relative max-w-md">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Rechercher par nom, courriel, ville, rôle..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full rounded-xl border pl-10 pr-4 py-3"
            />
          </div>

          {error && (
            <div className="mt-4 rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          )}

          {successMessage && (
            <div className="mt-4 rounded-xl bg-emerald-50 border border-emerald-200 px-4 py-3 text-sm text-emerald-700">
              {successMessage}
            </div>
          )}
        </div>

        <div className="bg-white rounded-3xl shadow-sm border overflow-hidden">
          {loading ? (
            <div className="p-8 text-slate-600">Chargement des utilisateurs...</div>
          ) : filteredUsers.length === 0 ? (
            <div className="p-8 text-slate-600">Aucun utilisateur trouvé.</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full text-sm">
                <thead className="bg-slate-100 text-slate-700">
                  <tr>
                    <th className="text-left px-4 py-3 font-semibold">ID</th>
                    <th className="text-left px-4 py-3 font-semibold">Prénom</th>
                    <th className="text-left px-4 py-3 font-semibold">Nom</th>
                    <th className="text-left px-4 py-3 font-semibold">Courriel</th>
                    <th className="text-left px-4 py-3 font-semibold">Téléphone</th>
                    <th className="text-left px-4 py-3 font-semibold">Ville</th>
                    <th className="text-left px-4 py-3 font-semibold">Rôle actuel</th>
                    <th className="text-left px-4 py-3 font-semibold">Nouveau rôle</th>
                    <th className="text-left px-4 py-3 font-semibold">Action</th>
                  </tr>
                </thead>

                <tbody>
                  {filteredUsers.map((u) => {
                    const roleChanged = Number(draftRoles[u.id]) !== Number(u.roleId);

                    return (
                      <tr key={u.id} className="border-t border-slate-200">
                        <td className="px-4 py-3">{u.id}</td>
                        <td className="px-4 py-3">{u.firstname || ""}</td>
                        <td className="px-4 py-3">{u.lastname || ""}</td>
                        <td className="px-4 py-3">{u.email || ""}</td>
                        <td className="px-4 py-3">{u.phone || "-"}</td>
                        <td className="px-4 py-3">{u.city || "-"}</td>
                        <td className="px-4 py-3">
                          <span className="inline-flex rounded-full bg-slate-100 px-3 py-1 font-medium text-slate-700">
                            {u.roleName || "-"}
                          </span>
                        </td>
                        <td className="px-4 py-3">
                          <select
                            value={draftRoles[u.id] ?? ""}
                            onChange={(e) => handleRoleChange(u.id, e.target.value)}
                            className="rounded-xl border px-3 py-2 min-w-[180px]"
                          >
                            <option value="">Choisir...</option>
                            {roles.map((role) => (
                              <option key={role.id} value={role.id}>
                                {role.name}
                              </option>
                            ))}
                          </select>
                        </td>
                        <td className="px-4 py-3">
                          <button
                            type="button"
                            disabled={!roleChanged || savingUserId === u.id}
                            onClick={() => saveRole(u)}
                            className="inline-flex items-center gap-2 rounded-xl bg-slate-900 px-4 py-2 text-white disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-800"
                          >
                            <Save className="w-4 h-4" />
                            {savingUserId === u.id ? "Sauvegarde..." : "Enregistrer"}
                          </button>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}