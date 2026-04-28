#!/bin/bash
echo "🚀 Démarrage complet de l’environnement GoCamp..."
echo "--------------------------------------------------"

# Charger ton environnement (Java, Gradle, MySQL dans le PATH, etc.)
source ~/.zshrc

# 🗄️  Démarrage de MySQL si pas déjà actif
MYSQL_STATUS=$(ps aux | grep -v grep | grep mysqld)
if [ -z "$MYSQL_STATUS" ]; then
  echo "🗄️  Démarrage du serveur MySQL..."
  sudo /usr/local/mysql-8.0.40-macos14-x86_64/support-files/mysql.server start
else
  echo "🗄️  MySQL est déjà en cours d’exécution."
fi

# ☕ Lancement du backend Spring Boot
if [ -d "$HOME/GoCampProjects/backend" ]; then
  echo "☕ Lancement du backend Spring Boot..."
  cd "$HOME/GoCampProjects/backend"
  ./gradlew bootRun > ~/GoCampProjects/backend/backend.log 2>&1 &
  BACK_PID=$!
  echo "   ➜ Backend démarré (PID $BACK_PID, logs : backend/backend.log)"
else
  echo "⚠️  Aucun dossier backend trouvé."
fi

# ⚡ Lancement du frontend React
if [ -d "$HOME/GoCampProjects/frontend" ]; then
  echo "⚡ Lancement du frontend React..."
  cd "$HOME/GoCampProjects/frontend"
  pnpm dev > ~/GoCampProjects/frontend/frontend.log 2>&1 &
  FRONT_PID=$!
  echo "   ➜ Frontend démarré (PID $FRONT_PID, logs : frontend/frontend.log)"
else
  echo "⚠️  Aucun dossier frontend trouvé."
fi

# 🧠 Ouverture du backend dans IntelliJ IDEA
if [ -d "$HOME/GoCampProjects/backend" ]; then
  echo "🧠 Ouverture du backend dans IntelliJ IDEA..."
  open -a "IntelliJ IDEA CE" "$HOME/GoCampProjects/backend"
fi

# 💻 Ouverture du frontend dans VS Code
if [ -d "$HOME/GoCampProjects/frontend" ]; then
  echo "💻 Ouverture du frontend dans VS Code..."
  code "$HOME/GoCampProjects/frontend"
fi

# 📬 Lancement de Postman
if [ -d "/Applications/Postman.app" ]; then
  echo "📬 Lancement de Postman..."
  open -a "Postman"
fi

echo ""
echo "🧩 Vérification des services..."
sleep 5  # petite pause pour laisser les serveurs démarrer

# 🔍 Vérification du backend (port 8080)
if nc -z localhost 8080 2>/dev/null; then
  echo "✅ Backend Spring Boot répond sur http://localhost:8080"
else
  echo "❌ Backend Spring Boot ne répond pas encore (port 8080)."
fi

# 🔍 Vérification du frontend (port 5173)
if nc -z localhost 5173 2>/dev/null; then
  echo "✅ Frontend React répond sur http://localhost:5173"
else
  echo "❌ Frontend React ne répond pas encore (port 5173)."
fi

echo "--------------------------------------------------"
echo "🎯 Environnement GoCamp prêt à l’emploi !"
