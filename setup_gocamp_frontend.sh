#!/bin/bash

echo "🚀 Installation automatique du frontend GoCamp..."
echo "-----------------------------------------------"

# 1. Vérification Node.js
if ! command -v node >/dev/null 2>&1; then
  echo "❌ Node.js n'est pas installé. Installe-le depuis https://nodejs.org/"
  exit 1
fi

# 2. Vérification NPM
if ! command -v npm >/dev/null 2>&1; then
  echo "❌ npm n'est pas installé."
  exit 1
fi

# 3. Création du projet si inexistant
PROJECT_NAME="frontend"

if [ ! -d "$PROJECT_NAME" ]; then
  echo "📦 Création du projet React avec Vite..."
  npm create vite@latest frontend -- --template react
else
  echo "ℹ️ Le dossier 'frontend' existe déjà, installation dans le dossier existant."
fi

cd frontend || exit

# 4. Installation des dépendances
echo "📥 Installation des dépendances..."
npm install

# 5. Installation TailwindCSS
echo "🎨 Installation de TailwindCSS..."
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p

# 6. Configuration tailwind.config.js
echo "🛠 Configuration de TailwindCSS..."

cat > tailwind.config.js << 'EOF'
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
EOF

# 7. Ajout de Tailwind dans index.css
echo "🎨 Injection des directives Tailwind dans index.css..."

cat > src/index.css << 'EOF'
@tailwind base;
@tailwind components;
@tailwind utilities;

body {
  font-family: system-ui, sans-serif;
}
EOF

# 8. Création des répertoires
echo "📁 Création des répertoires /src et /public..."

mkdir -p src/pages
mkdir -p src/components
mkdir -p src/assets/images
mkdir -p src/assets/icons

mkdir -p public/images
mkdir -p public/logos
mkdir -p public/backgrounds

# 9. Création d'une HomePage vide
echo "📝 Création du composant HomePage.jsx..."

cat > src/pages/HomePage.jsx << 'EOF'
export default function HomePage() {
  return (
    <div className="min-h-screen bg-gray-50">
      <h1 className="text-4xl font-bold text-center pt-20">
        Page d'accueil GoCamp
      </h1>
      <p className="text-center mt-4 text-gray-600">
        Votre page réagira lorsque vous ajouterez le code complet fourni par ChatGPT.
      </p>
    </div>
  );
}
EOF

# 10. Modification de App.jsx pour afficher HomePage
echo "⚙️ Configuration de App.jsx..."

cat > src/App.jsx << 'EOF'
import HomePage from "./pages/HomePage";

function App() {
  return (
    <HomePage />
  );
}

export default App;
EOF

# 11. Nettoyage du template vite
echo "🧹 Nettoyage des fichiers inutiles..."

rm -f src/App.css
rm -f src/assets/react.svg

# 12. Fin
echo "🎉 Installation terminée !"

echo ""
echo "📌 Pour démarrer le projet :"
echo "cd frontend"
echo "npm run dev"
echo ""
echo "🌐 Le site sera visible sur : http://localhost:5173/"
echo ""
echo "🔥 Le frontend GoCamp est prêt à l’usage !"

