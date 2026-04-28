#!/bin/bash
# --- Configuration d'environnement (GoCamp) ---
export PATH="/usr/local/mysql-8.0.40-macos14-x86_64/bin:$PATH"

echo "🛑 Arrêt complet de l’environnement GoCamp..."
echo "--------------------------------------------------"

BASE_DIR="$HOME/GoCampProjects"

# Charger les variables d'environnement
source ~/.zshrc

# ☕ Arrêter le backend Spring Boot (Gradle)
echo "☕ Fermeture du backend Spring Boot..."
pkill -f "gradle" >/dev/null 2>&1
pkill -f "bootRun" >/dev/null 2>&1

# ⚡ Arrêter le frontend React (Vite / pnpm)
echo "⚡ Fermeture du frontend React..."
pkill -f "vite" >/dev/null 2>&1
pkill -f "react-scripts" >/dev/null 2>&1
pkill -f "pnpm" >/dev/null 2>&1

# 🗄️ Arrêter MySQL proprement
echo "🗄️  Arrêt du serveur MySQL..."
sudo /usr/local/mysql-8.0.40-macos14-x86_64/support-files/mysql.server stop >/dev/null 2>&1

# 💻 Fermer VS Code
echo "💻 Fermeture de Visual Studio Code..."
pkill -f "Visual Studio Code" >/dev/null 2>&1

# 🧠 Fermer IntelliJ IDEA
echo "🧠 Fermeture d’IntelliJ IDEA..."
pkill -f "IntelliJ IDEA" >/dev/null 2>&1

# 📬 Fermer Postman
echo "📬 Fermeture de Postman..."
pkill -f "Postman" >/dev/null 2>&1

# Nettoyer les logs temporaires (optionnel)
echo "🧹 Nettoyage des logs temporaires..."
find "$BASE_DIR/backend" -name "*.log" -type f -delete
find "$BASE_DIR/frontend" -name "*.log" -type f -delete

echo "--------------------------------------------------"
echo "✅ Tous les services et applications GoCamp ont été arrêtés proprement."
echo "--------------------------------------------------"

