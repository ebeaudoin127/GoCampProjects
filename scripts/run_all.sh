
#!/bin/bash

# ============================================================
# Fichier : start_gocamp.sh
# Dernière modification : 2026-04-28
# Auteur : ChatGPT (modifications pour Eric Beaudoin)
#
# Résumé :
#   - Backend et frontend démarrent chacun dans une nouvelle
#     fenêtre Terminal macOS (osascript)
#   - restart_backend / restart_frontend tuent le process
#     sur le port et relancent dans une nouvelle fenêtre
#   - Les logs continuent d’être écrits dans backend.log
#     et frontend.log via `tee`
#   - Ajout d’une option pour créer un ZIP du projet
#   - Ajout d’une option pour créer un TAR.GZ du projet
#   - Le ZIP/TAR.GZ exclut les dossiers lourds/inutiles :
#     node_modules, dist, build, .git, logs, caches, etc.
#   - Ajout d’une option pour compiler tout le Java du backend
#   - Fermeture automatique et robuste des fenêtres Terminal
#     backend/frontend ouvertes par le script
#   - Correction AppleScript pour éviter les erreurs de syntaxe
# ============================================================

#########################################################
#               GoCamp - Gestion complète PRO           #
#                 macOS + Spring + React                #
#########################################################

BASE_DIR="$HOME/GoCampProjects"
BACKEND_DIR="$BASE_DIR/backend"
FRONTEND_DIR="$BASE_DIR/frontend"
LOG_DIR="$BASE_DIR/logs"
ARCHIVE_DIR="$BASE_DIR/archives"

MYSQL_USER="root"
MYSQL_PASS="eric"
MYSQL_DB="reservecamping"
MYSQL_PATH="/usr/local/mysql-8.0.40-macos14-x86_64"

BACKEND_PORT=8080
FRONTEND_PORT=5173

BACKEND_WINDOW_TITLE="GoCamp Backend"
FRONTEND_WINDOW_TITLE="GoCamp Frontend"

export PATH="$MYSQL_PATH/bin:$PATH"

RED="\033[0;31m"
GREEN="\033[0;32m"
YELLOW="\033[0;33m"
BLUE="\033[0;34m"
MAGENTA="\033[0;35m"
CYAN="\033[0;36m"
BOLD="\033[1m"
RESET="\033[0m"

log_info()  { echo -e "${BLUE}ℹ️  $1${RESET}"; }
log_ok()    { echo -e "${GREEN}✅ $1${RESET}"; }
log_warn()  { echo -e "${YELLOW}⚠️  $1${RESET}"; }
log_err()   { echo -e "${RED}❌ $1${RESET}"; }
log_title() { echo -e "\n${MAGENTA}${BOLD}$1${RESET}"; }

ensure_dirs() {
  mkdir -p "$LOG_DIR"
  mkdir -p "$ARCHIVE_DIR"
}

rotate_log() {
  local log_file="$1"
  local name="$2"

  if [ -f "$log_file" ]; then
    local ts
    ts=$(date +"%Y%m%d_%H%M%S")
    mv "$log_file" "$LOG_DIR/${name}_${ts}.log"
    log_info "Ancien log sauvegardé : ${LOG_DIR}/${name}_${ts}.log"
  fi
}

check_command() {
  local cmd="$1"
  local label="$2"

  if command -v "$cmd" >/dev/null 2>&1; then
    local version
    case "$cmd" in
      node)  version=$("$cmd" -v 2>/dev/null) ;;
      java)  version=$("$cmd" -version 2>/dev/null | head -n1) ;;
      mysql) version=$("$cmd" --version 2>/dev/null) ;;
      pnpm)  version=$("$cmd" -v 2>/dev/null) ;;
      zip)   version=$("$cmd" -v 2>/dev/null | head -n1) ;;
      tar)   version="(ok)" ;;
      *)     version="(ok)" ;;
    esac
    log_ok "$label détecté : $version"
  else
    log_err "$label introuvable (commande '$cmd' absente du PATH)"
  fi
}

close_named_terminal_windows() {
  local title="$1"

  osascript <<EOF >/dev/null 2>&1
tell application "Terminal"
  repeat with w in (every window)
    try
      set theTab to selected tab of w
      set tabTitle to custom title of theTab
      if tabTitle is "$title" then
        try
          do script "exit" in theTab
          delay 0.2
        end try
        close w saving no
      end if
    end try
  end repeat
end tell
EOF
}

close_gocamp_terminal_windows() {
  log_info "Fermeture des fenêtres Terminal GoCamp..."
  close_named_terminal_windows "$BACKEND_WINDOW_TITLE"
  close_named_terminal_windows "$FRONTEND_WINDOW_TITLE"
}

check_system() {
  log_title "🔍 Vérifications système"

  if [[ "$OSTYPE" == "darwin"* ]]; then
    log_ok "macOS détecté"
  else
    log_warn "OS non macOS détecté : $OSTYPE"
  fi

  check_command "node" "Node.js"
  check_command "pnpm" "pnpm"
  check_command "java" "Java"
  check_command "mysql" "MySQL"
  check_command "lsof" "lsof"
  check_command "nc" "netcat"
  check_command "zip" "zip"
  check_command "tar" "tar"

  if [ -d "$BACKEND_DIR" ]; then log_ok "Dossier backend OK : $BACKEND_DIR"
  else log_err "Dossier backend manquant : $BACKEND_DIR"; fi

  if [ -d "$FRONTEND_DIR" ]; then log_ok "Dossier frontend OK : $FRONTEND_DIR"
  else log_err "Dossier frontend manquant : $FRONTEND_DIR"; fi
}

start_mysql() {
  log_title "🗄️  Démarrage MySQL"

  if pgrep mysqld >/dev/null 2>&1; then
    log_ok "MySQL déjà en marche"
  else
    log_info "Démarrage MySQL..."
    sudo "$MYSQL_PATH/support-files/mysql.server" start
    sleep 4
  fi
}

start_backend() {
  log_title "☕ Démarrage backend"

  if lsof -i :"$BACKEND_PORT" >/dev/null 2>&1; then
    pid=$(lsof -ti :"$BACKEND_PORT")
    log_warn "Port backend $BACKEND_PORT occupé → kill $pid"
    kill -9 "$pid"
    sleep 2
  fi

  ensure_dirs
  rotate_log "$BACKEND_DIR/backend.log" "backend"

  osascript <<EOF
tell application "Terminal"
  activate
  set newTab to do script "cd $BACKEND_DIR && ./gradlew bootRun | tee backend.log"
  delay 0.2
  set custom title of newTab to "$BACKEND_WINDOW_TITLE"
end tell
EOF

  log_ok "Backend lancé dans une nouvelle fenêtre Terminal"
}

stop_backend() {
  log_title "🛑 Arrêt backend"

  if lsof -i :"$BACKEND_PORT" >/dev/null 2>&1; then
    pid=$(lsof -ti :"$BACKEND_PORT")
    kill -9 "$pid"
    log_ok "Backend arrêté (PID $pid)"
  else
    log_warn "Backend déjà arrêté"
  fi
}

restart_backend() {
  stop_backend
  close_named_terminal_windows "$BACKEND_WINDOW_TITLE"
  sleep 2
  start_backend
}

start_frontend() {
  log_title "⚡ Démarrage frontend"

  if lsof -i :"$FRONTEND_PORT" >/dev/null 2>&1; then
    pid=$(lsof -ti :"$FRONTEND_PORT")
    log_warn "Port frontend $FRONTEND_PORT occupé → kill $pid"
    kill -9 "$pid"
    sleep 2
  fi

  ensure_dirs
  rotate_log "$FRONTEND_DIR/frontend.log" "frontend"

  osascript <<EOF
tell application "Terminal"
  activate
  set newTab to do script "cd $FRONTEND_DIR && pnpm dev | tee frontend.log"
  delay 0.2
  set custom title of newTab to "$FRONTEND_WINDOW_TITLE"
end tell
EOF

  log_ok "Frontend lancé dans une nouvelle fenêtre Terminal"
}

stop_frontend() {
  log_title "🛑 Arrêt frontend"

  if lsof -i :"$FRONTEND_PORT" >/dev/null 2>&1; then
    pid=$(lsof -ti :"$FRONTEND_PORT")
    kill -9 "$pid"
    log_ok "Frontend arrêté (PID $pid)"
  else
    log_warn "Frontend déjà arrêté"
  fi
}

restart_frontend() {
  stop_frontend
  close_named_terminal_windows "$FRONTEND_WINDOW_TITLE"
  sleep 2
  start_frontend
}

stop_all() {
  log_title "🛑 Arrêt complet GoCamp"
  stop_backend
  stop_frontend
  pkill -f gradle >/dev/null 2>&1
  pkill -f vite >/dev/null 2>&1
  close_gocamp_terminal_windows
  log_ok "Tous les services sont arrêtés."
}

compile_backend_java() {
  log_title "🧪 Compilation du Java backend"

  (
    cd "$BACKEND_DIR" || exit 1
    ./gradlew compileJava
  )

  if [ $? -eq 0 ]; then
    log_ok "Compilation Java réussie"
  else
    log_err "La compilation Java a échoué"
    return 1
  fi
}

check_services() {
  log_title "🧠 Vérification des services"

  if nc -z localhost "$BACKEND_PORT" 2>/dev/null; then
    log_ok "Backend actif → http://localhost:$BACKEND_PORT"
    BACKEND_READY=true
  else
    log_err "Backend inactif"
    BACKEND_READY=false
  fi

  if nc -z localhost "$FRONTEND_PORT" 2>/dev/null; then
    log_ok "Frontend actif → http://localhost:$FRONTEND_PORT"
    FRONTEND_READY=true
  else
    log_err "Frontend inactif"
    FRONTEND_READY=false
  fi
}

open_browser() {
  [ "$FRONTEND_READY" = true ] && open "http://localhost:$FRONTEND_PORT"
  [ "$BACKEND_READY" = true ] && open "http://localhost:$BACKEND_PORT"
}

show_backend_logs() {
  log_title "📜 Logs backend"
  [ -f "$BACKEND_DIR/backend.log" ] && tail -n 50 "$BACKEND_DIR/backend.log" || log_warn "Aucun log backend."
}

show_frontend_logs() {
  log_title "📜 Logs frontend"
  [ -f "$FRONTEND_DIR/frontend.log" ] && tail -n 50 "$FRONTEND_DIR/frontend.log" || log_warn "Aucun log frontend."
}

clean_logs() {
  log_title "🧹 Nettoyage des logs"
  rm -f "$BACKEND_DIR/backend.log"
  rm -f "$FRONTEND_DIR/frontend.log"
  rm -f "$LOG_DIR"/*.log
  log_ok "Tous les logs ont été supprimés."
}

debug_mode() {
  log_title "🐛 Mode debug"
  [ -f "$BACKEND_DIR/backend.log" ] && tail -f "$BACKEND_DIR/backend.log" &
  T1=$!
  [ -f "$FRONTEND_DIR/frontend.log" ] && tail -f "$FRONTEND_DIR/frontend.log" &
  T2=$!
  wait
  kill "$T1" "$T2" 2>/dev/null
}

clear_caches() {
  log_title "🗑️  Vidage des caches"
  rm -rf ~/.gradle/caches ~/.gradle/daemon ~/.gradle/native
  pnpm store prune
  rm -rf "$FRONTEND_DIR/node_modules/.vite"
  log_ok "Caches nettoyés"
}

create_project_zip() {
  log_title "📦 ZIP GoCamp (code source propre)"
  ensure_dirs

  local ts
  ts=$(date +"%Y%m%d_%H%M%S")
  local zip_file="$ARCHIVE_DIR/gocamp_code_only_${ts}.zip"
  local file_list
  file_list=$(mktemp)

  (
    cd "$BASE_DIR" || exit 1

    find backend/src/main/java/com/gocamp frontend/src \
      -type f \
      \( -name "*.java" -o -name "*.js" -o -name "*.jsx" \) \
      -not -path "*/node_modules/*" \
      -not -path "*/build/*" \
      -not -path "*/dist/*" \
      -not -path "*/.gradle/*" \
      -not -path "*/target/*" \
      -not -name ".DS_Store" \
      | sort > "$file_list"

    zip -q -X "$zip_file" -@ < "$file_list"
    rm -f "$file_list"
    unzip -t "$zip_file" >/dev/null
  )

  if [ $? -eq 0 ]; then
    log_ok "ZIP créé et valide"
    echo -e "${CYAN}📁 Archive : ${BOLD}$zip_file${RESET}"
    unzip -l "$zip_file" | head -30
  else
    log_err "ZIP invalide"
    return 1
  fi
}


create_project_tar() {
  log_title "📦 TAR.GZ GoCamp (code source propre)"

  ensure_dirs

  if [ ! -d "$BASE_DIR" ]; then
    log_err "Dossier de base introuvable : $BASE_DIR"
    return 1
  fi

  if ! command -v tar >/dev/null 2>&1; then
    log_err "La commande 'tar' est introuvable."
    return 1
  fi

  local ts
  ts=$(date +"%Y%m%d_%H%M%S")

  local tar_file="$ARCHIVE_DIR/gocamp_code_only_${ts}.tar.gz"

  log_info "Création : $tar_file"
  log_info "Backend : backend/src/main/java/com/gocamp"
  log_info "Frontend : frontend/src"

  (
    cd "$BASE_DIR" || exit 1

    tar \
      --exclude="*/node_modules/*" \
      --exclude="*/build/*" \
      --exclude="*/dist/*" \
      --exclude="*/.gradle/*" \
      --exclude="*/target/*" \
      --exclude=".DS_Store" \
      -czf "$tar_file" \
      backend/src/main/java/com/gocamp \
      frontend/src
  )

  if [ $? -eq 0 ] && [ -f "$tar_file" ]; then
    log_ok "TAR.GZ créé avec succès"
    echo -e "${CYAN}📁 Archive : ${BOLD}$tar_file${RESET}"
    tar -tzf "$tar_file" | head -30
  else
    log_err "Échec de la création du TAR.GZ"
    return 1
  fi
}

start_all() {
  log_title "🚀 Démarrage complet GoCamp"
  source ~/.zshrc >/dev/null 2>&1 || true
  start_mysql || return 1
  start_backend
  start_frontend
  sleep 5
  check_services
  open_browser
}

restart_all() {
  stop_all
  sleep 2
  start_all
}

show_menu() {
  echo -e ""
  echo -e "${BOLD}=============================================${RESET}"
  echo -e "${BOLD}          GoCamp - Gestion complète          ${RESET}"
  echo -e "${BOLD}=============================================${RESET}"
  echo "1) Démarrer tout"
  echo "2) Redémarrer tout"
  echo "3) Arrêter tout"
  echo "4) Vérifier l'état des services"
  echo "5) Logs backend (50 dernières lignes)"
  echo "6) Logs frontend (50 dernières lignes)"
  echo "7) Mode DEBUG (logs live)"
  echo "8) Vérifications système"
  echo "9) Redémarrer seulement le backend"
  echo "10) Redémarrer seulement le frontend"
  echo "11) Nettoyer tous les logs"
  echo "12) Vider les caches (Gradle / pnpm / Vite)"
  echo "13) Créer un ZIP du projet"
  echo "14) Compiler tout le Java du backend"
  echo "15) Quitter"
  echo "16) Créer un TAR.GZ du projet"
  echo ""
}

main() {
  clear
  log_title "🎯 GoCamp - Outil de gestion PRO (macOS)"
  ensure_dirs

  while true; do
    show_menu
    read -rp "👉 Ton choix : " choice

    case "$choice" in
      1) start_all ;;
      2) restart_all ;;
      3) stop_all ;;
      4) check_services ;;
      5) show_backend_logs ;;
      6) show_frontend_logs ;;
      7) debug_mode ;;
      8) check_system ;;
      9) restart_backend ;;
      10) restart_frontend ;;
      11) clean_logs ;;
      12) clear_caches ;;
      13) create_project_zip ;;
      14) compile_backend_java ;;
      15) echo -e "${GREEN}Au revoir 👋${RESET}"; exit 0 ;;
      16) create_project_tar ;;
      *) log_warn "Choix invalide." ;;
    esac
  done
}

main