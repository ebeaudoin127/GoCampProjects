#!/bin/bash
echo "🛑 Fermeture complète de l’environnement GoCamp..."
echo "--------------------------------------------------"

# Fermer les processus backend Spring Boot
echo "☕ Arrêt du backend Spring Boot..."
pkill -f "gradle" >/dev/null 2>&1
pkill -f "bootRun" >/dev/null 2>&1

# Fermer les processus frontend React (Vite)
echo "⚡ Arrêt du frontend React..."
pkill -f "vite" >/dev/null 2>&1
pkill -f "react-scripts" >/dev/null 2>&1
pkill -f "pnpm" >/dev/null 2>&1

# Fermer IntelliJ IDEA
echo "🧠 Fermeture d’IntelliJ IDEA..."
pkill -f "IntelliJ IDEA" >/dev/null 2>&1

# Fermer VS Code
echo "💻 Fermeture de VS Code..."
pkill -f "Visual Studio Code" >/dev/null 2>&1

# Fermer Postman
echo "📬 Fermeture de Postman..."
pkill -f "Postman" >/dev/null 2>&1

# Arrêter MySQL
echo "🗄️  Arrêt du serveur MySQL..."
sudo /usr/local/mysql-8.0.40-macos14-x86_64/support-files/mysql.server stop

echo "✅ Tous les services et applications GoCamp sont arrêtés."
echo "--------------------------------------------------"
