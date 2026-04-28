#!/bin/bash
echo "🚧 Initialisation du projet GoCamp / ReserveCamping..."
echo "-------------------------------------------------------"

BASE_DIR="$HOME/GoCampProjects"

# Charger ton environnement
source ~/.zshrc

# Créer la structure principale
mkdir -p "$BASE_DIR"/{backend,frontend,database,docs}

# -------------------------------------------------------
# 🧱 BACKEND : Initialisation d’un projet Spring Boot / Gradle
# -------------------------------------------------------
cd "$BASE_DIR/backend" || exit

echo "☕ Création du projet Spring Boot (backend)..."

# Initialiser un build.gradle minimal
cat > build.gradle <<'EOF'
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.3.2'
    id 'io.spring.dependency-management' version '1.1.5'
}

group = 'com.gocamp'
version = '1.0.0'
java {
    sourceCompatibility = '21'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'io.jsonwebtoken:jjwt-api:0.12.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.5'
    runtimeOnly 'com.mysql:mysql-connector-j:8.3.0'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
    useJUnitPlatform()
}
EOF

# settings.gradle
cat > settings.gradle <<'EOF'
rootProject.name = 'ReserveCamping'
EOF

# Arborescence Java
mkdir -p src/main/java/com/gocamp/reservecamping
mkdir -p src/main/resources

# Exemple d’application Spring Boot
cat > src/main/java/com/gocamp/reservecamping/ReserveCampingApplication.java <<'EOF'
package com.gocamp.reservecamping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReserveCampingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReserveCampingApplication.class, args);
    }
}
EOF

# application.properties
cat > src/main/resources/application.properties <<'EOF'
spring.datasource.url=jdbc:mysql://localhost:3306/reservecamping?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
EOF

# -------------------------------------------------------
# ⚡ FRONTEND : React + Tailwind + pnpm
# -------------------------------------------------------
cd "$BASE_DIR" || exit
echo "⚡ Création du projet React + Tailwind (frontend)..."
cd frontend
pnpm create vite@latest . --template react > /dev/null 2>&1
pnpm install -D tailwindcss postcss autoprefixer > /dev/null 2>&1
npx tailwindcss init -p > /dev/null 2>&1

# Configurer Tailwind
cat > tailwind.config.js <<'EOF'
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
EOF

# Ajouter import Tailwind
cat > src/index.css <<'EOF'
@tailwind base;
@tailwind components;
@tailwind utilities;
EOF

# -------------------------------------------------------
# 🗄️ DATABASE : script SQL initial
# -------------------------------------------------------
echo "🗄️  Création du script SQL de base..."
cat > "$BASE_DIR/database/init.sql" <<'EOF'
CREATE DATABASE IF NOT EXISTS reservecamping CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE reservecamping;

CREATE TABLE IF NOT EXISTS user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  prenom VARCHAR(50),
  nom VARCHAR(50),
  courriel VARCHAR(100) UNIQUE,
  mot_de_passe VARCHAR(255),
  role ENUM('UTILISATEUR','GESTIONNAIRE','CAMPING_ADMIN','SUPER_ADMIN') DEFAULT 'UTILISATEUR'
);
EOF

# -------------------------------------------------------
# 📘 DOCS : structure initiale
# -------------------------------------------------------
echo "📘 Création du fichier de documentation API..."
cat > "$BASE_DIR/docs/API.md" <<'EOF'
# API ReserveCamping

## Authentification
- `POST /auth/login` : connexion utilisateur
- `POST /auth/register` : création de compte

## Campings
- `GET /campgrounds` : liste des campings
- `GET /campgrounds/{id}` : détails d’un camping
- `POST /campgrounds` : ajout (ADMIN)

## Réservations
- `GET /reservations` : liste des réservations
- `POST /reservations` : créer une réservation
EOF

echo "-------------------------------------------------------"
echo "✅ Projet GoCamp / ReserveCamping initialisé avec succès !"
echo "📁 Dossiers créés sous : $BASE_DIR"
echo "☕ Backend : Spring Boot + Gradle"
echo "⚡ Frontend : React + Tailwind (pnpm)"
echo "🗄️  Base de données : reservecamping (init.sql)"
echo "📘 Documentation : docs/API.md"
echo "-------------------------------------------------------"
