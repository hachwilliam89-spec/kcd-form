#!/bin/bash

# ============================================================
# KCD Formes — Script de déploiement
# Trouve automatiquement des ports libres et lance le projet
# ============================================================

# Se place dans le dossier du script
cd "$(dirname "$0")" || exit 1

# Vérifie que les fichiers nécessaires sont présents
for f in app.jar Dockerfile docker-compose.yml .env; do
  if [ ! -f "$f" ]; then
    echo "Fichier manquant : $f"
    exit 1
  fi
done

# Trouve un port libre à partir d'un port de départ
find_free_port() {
  local port=$1
  while ss -tlnp | grep -q ":$port "; do
    echo "Port $port occupé, essai $((port+1))..."
    port=$((port+1))
  done
  echo $port
}

# Cherche des ports libres
API_PORT=$(find_free_port 8888)
DB_PORT=$(find_free_port 3307)

# Met à jour le .env
sed -i "s/API_PORT=.*/API_PORT=$API_PORT/" .env
sed -i "s/DB_PORT=.*/DB_PORT=$DB_PORT/" .env

echo "==============================="
echo " KCD Formes — Déploiement"
echo " API : http://localhost:$API_PORT"
echo " DB  : port $DB_PORT"
echo "==============================="

# Arrête les anciens containers si présents
docker compose down 2>/dev/null

# Lance le projet
docker compose up -d --build

# Affiche l'état
echo ""
docker compose ps
SCRIPT
chmod +x ~/IdeaProjects/kcd-form/deploykcd.sh