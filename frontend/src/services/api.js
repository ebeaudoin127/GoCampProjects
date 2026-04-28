
// ============================================================
// Fichier : frontend/src/services/api.js
// Dernière modification : 2026-04-20
// Auteur : ChatGPT
//
// Résumé des modifications :
// - Conservation de safeJson()
// - Amélioration importante de la gestion des erreurs
// - Extraction propre de error/message depuis le backend
// - Support complet GET / POST / PUT / DELETE / LOGIN
// - Ajout de la méthode DELETE pour la suppression
//   des règles tarifaires et autres ressources
// ============================================================

const API_URL = "http://localhost:8080/api";

// ----------------------------------------------------
// Parse JSON seulement si body non vide
// ----------------------------------------------------
async function safeJson(res) {
  const text = await res.text();

  if (!text || text.trim().length === 0) {
    return {};
  }

  try {
    return JSON.parse(text);
  } catch (err) {
    console.error("❌ JSON parse error:", err, text);
    throw new Error("Réponse JSON invalide du serveur.");
  }
}

// ----------------------------------------------------
// Extraire un message d'erreur lisible
// ----------------------------------------------------
async function extractErrorMessage(res, fallbackMessage) {
  try {
    const data = await safeJson(res);

    if (typeof data === "string" && data.trim()) {
      return data;
    }

    if (data?.error) {
      return data.error;
    }

    if (data?.message) {
      return data.message;
    }

    return fallbackMessage;
  } catch (err) {
    console.error("❌ extractErrorMessage error:", err);
    return fallbackMessage;
  }
}

// ----------------------------------------------------
// Headers avec token
// ----------------------------------------------------
function getHeaders() {
  const token = localStorage.getItem("token");

  return token
    ? {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      }
    : {
        "Content-Type": "application/json",
      };
}

// ----------------------------------------------------
// GET
// ----------------------------------------------------
async function get(url) {
  const res = await fetch(API_URL + url, {
    method: "GET",
    headers: getHeaders(),
  });

  if (!res.ok) {
    const message = await extractErrorMessage(res, "Erreur API GET");
    console.error("❌ GET error:", res.status, message);
    throw new Error(message);
  }

  return safeJson(res);
}

// ----------------------------------------------------
// POST
// ----------------------------------------------------
async function post(url, body) {
  const res = await fetch(API_URL + url, {
    method: "POST",
    headers: getHeaders(),
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const message = await extractErrorMessage(res, "Erreur API POST");
    console.error("❌ POST error:", res.status, message);
    throw new Error(message);
  }

  return safeJson(res);
}

// ----------------------------------------------------
// PUT
// ----------------------------------------------------
async function put(url, body) {
  const res = await fetch(API_URL + url, {
    method: "PUT",
    headers: getHeaders(),
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    const message = await extractErrorMessage(res, "Erreur API PUT");
    console.error("❌ PUT error:", res.status, message);
    throw new Error(message);
  }

  return safeJson(res);
}

// ----------------------------------------------------
// DELETE
// ----------------------------------------------------
async function remove(url) {
  const res = await fetch(API_URL + url, {
    method: "DELETE",
    headers: getHeaders(),
  });

  if (!res.ok) {
    const message = await extractErrorMessage(res, "Erreur API DELETE");
    console.error("❌ DELETE error:", res.status, message);
    throw new Error(message);
  }

  return safeJson(res);
}

// ----------------------------------------------------
// LOGIN
// ----------------------------------------------------
async function login(email, password) {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  if (!res.ok) {
    const message = await extractErrorMessage(res, "Erreur API LOGIN");
    console.error("❌ LOGIN error:", res.status, message);
    throw new Error(message);
  }

  return safeJson(res);
}

// ----------------------------------------------------
const api = {
  get,
  post,
  put,
  delete: remove,
  login,
};

export default api;
