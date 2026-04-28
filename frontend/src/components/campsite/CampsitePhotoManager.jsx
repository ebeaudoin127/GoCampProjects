
// ============================================================
// Fichier : CampsitePhotoManager.jsx
// Chemin : frontend/src/components/campsite
// Dernière modification : 2026-04-18
//
// Résumé :
// - Gestion complète des photos d’un site
// - Upload (max 3)
// - Suppression
// - Définir photo principale
// - Correction affichage image via backend:8080
// ============================================================

import React, { useEffect, useState } from "react";
import api from "../../services/api";

const API_BASE = "http://localhost:8080/api";
const API_SERVER = "http://localhost:8080";

function buildFileUrl(path) {
  if (!path) return "";
  if (path.startsWith("http://") || path.startsWith("https://")) return path;
  return `${API_SERVER}${path}`;
}

export default function CampsitePhotoManager({ campsiteId }) {
  const [photos, setPhotos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [uploading, setUploading] = useState(false);
  const [busyPhotoId, setBusyPhotoId] = useState(null);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const loadPhotos = async () => {
    setLoading(true);
    setError("");

    try {
      const data = await api.get(`/campsites/${campsiteId}/photos`);
      setPhotos(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error(err);
      setError("Impossible de charger les photos du site.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!campsiteId) return;
    loadPhotos();
  }, [campsiteId]);

  const handleUpload = async (e) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setUploading(true);
    setError("");
    setSuccessMessage("");

    try {
      const formData = new FormData();
      formData.append("file", file);

      const token = localStorage.getItem("token");

      const res = await fetch(`${API_BASE}/campsites/${campsiteId}/photos`, {
        method: "POST",
        headers: token ? { Authorization: `Bearer ${token}` } : undefined,
        body: formData,
      });

      const text = await res.text();
      let payload = {};
      if (text && text.trim().length > 0) {
        try {
          payload = JSON.parse(text);
        } catch {
          payload = { message: text };
        }
      }

      if (!res.ok) {
        throw new Error(payload.message || text || "Erreur lors de l’upload.");
      }

      setSuccessMessage("Photo ajoutée avec succès.");
      await loadPhotos();
      e.target.value = "";
    } catch (err) {
      console.error(err);
      setError(err.message || "Erreur lors de l’upload.");
    } finally {
      setUploading(false);
    }
  };

  const deletePhoto = async (photoId) => {
    setBusyPhotoId(photoId);
    setError("");
    setSuccessMessage("");

    try {
      const token = localStorage.getItem("token");

      const res = await fetch(`${API_BASE}/campsite-photos/${photoId}`, {
        method: "DELETE",
        headers: token ? { Authorization: `Bearer ${token}` } : undefined,
      });

      const text = await res.text();
      let payload = {};
      if (text && text.trim().length > 0) {
        try {
          payload = JSON.parse(text);
        } catch {
          payload = { message: text };
        }
      }

      if (!res.ok) {
        throw new Error(payload.message || text || "Erreur lors de la suppression.");
      }

      setSuccessMessage("Photo supprimée avec succès.");
      await loadPhotos();
    } catch (err) {
      console.error(err);
      setError(err.message || "Erreur lors de la suppression.");
    } finally {
      setBusyPhotoId(null);
    }
  };

  const setPrimary = async (photoId) => {
    setBusyPhotoId(photoId);
    setError("");
    setSuccessMessage("");

    try {
      await api.put(`/campsite-photos/${photoId}/primary`);
      setSuccessMessage("Photo principale mise à jour.");
      await loadPhotos();
    } catch (err) {
      console.error(err);
      setError(err.message || "Erreur lors du changement de photo principale.");
    } finally {
      setBusyPhotoId(null);
    }
  };

  return (
    <div className="space-y-4">
      <div>
        <h3 className="text-base font-semibold text-slate-900">Photos du site</h3>
        <p className="text-sm text-slate-500 mt-1">
          Maximum 3 photos par site.
        </p>
      </div>

      {photos.length < 3 && (
        <div className="rounded-2xl border bg-slate-50 p-4">
          <label className="block text-sm font-medium text-slate-700 mb-2">
            Ajouter une photo
          </label>

          <input
            type="file"
            accept=".jpg,.jpeg,.png,.webp,image/jpeg,image/png,image/webp"
            onChange={handleUpload}
            disabled={uploading}
            className="block w-full text-sm text-slate-700"
          />

          <p className="text-xs text-slate-500 mt-2">
            Formats acceptés : JPG, JPEG, PNG, WEBP. Maximum 5 Mo.
          </p>
        </div>
      )}

      {uploading && (
        <div className="rounded-xl bg-blue-50 border border-blue-200 px-4 py-3 text-sm text-blue-700">
          Upload en cours...
        </div>
      )}

      {error && (
        <div className="rounded-xl bg-red-50 border border-red-200 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}

      {successMessage && (
        <div className="rounded-xl bg-emerald-50 border border-emerald-200 px-4 py-3 text-sm text-emerald-700">
          {successMessage}
        </div>
      )}

      {loading ? (
        <div className="rounded-xl border bg-white px-4 py-6 text-sm text-slate-600">
          Chargement des photos...
        </div>
      ) : photos.length === 0 ? (
        <div className="rounded-xl border bg-white px-4 py-6 text-sm text-slate-600">
          Aucune photo pour ce site.
        </div>
      ) : (
        <div className="grid gap-4">
          {photos.map((photo) => (
            <div
              key={photo.id}
              className="rounded-2xl border bg-white p-3 shadow-sm"
            >
              <img
                src={buildFileUrl(photo.thumbnailPath || photo.filePath)}
                alt={photo.captionFr || `Photo ${photo.id}`}
                className="w-full rounded-xl object-cover"
              />

              <div className="mt-3 flex items-center justify-between gap-3">
                <div>
                  <p className="text-sm font-medium text-slate-900">
                    {photo.captionFr || "Photo du site"}
                  </p>
                  <p className="text-xs text-slate-500">
                    {photo.isPrimary ? "Photo principale" : "Photo secondaire"}
                  </p>
                </div>

                {photo.isPrimary && (
                  <span className="rounded-full bg-emerald-100 px-3 py-1 text-xs font-medium text-emerald-700">
                    Principale
                  </span>
                )}
              </div>

              <div className="mt-3 flex gap-2">
                {!photo.isPrimary && (
                  <button
                    type="button"
                    onClick={() => setPrimary(photo.id)}
                    disabled={busyPhotoId === photo.id}
                    className="rounded-xl bg-blue-600 px-3 py-2 text-xs font-medium text-white hover:bg-blue-700 disabled:opacity-50"
                  >
                    Définir principale
                  </button>
                )}

                <button
                  type="button"
                  onClick={() => deletePhoto(photo.id)}
                  disabled={busyPhotoId === photo.id}
                  className="rounded-xl bg-red-600 px-3 py-2 text-xs font-medium text-white hover:bg-red-700 disabled:opacity-50"
                >
                  Supprimer
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}