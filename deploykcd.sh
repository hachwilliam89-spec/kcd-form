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
    echo "Port $port occupé, essai $((port+1))..." >&2
    port=$((port+1))
  done
  echo $port
}

# Détecte l'IP de la machine
SERVER_IP=$(hostname -I | awk '{print $1}')

# Cherche des ports libres
API_PORT=$(find_free_port 8888)
DB_PORT=$(find_free_port 3307)
FRONT_PORT=$(find_free_port 3000)

# Met à jour le .env
sed -i "s/API_PORT=.*/API_PORT=$API_PORT/" .env
sed -i "s/DB_PORT=.*/DB_PORT=$DB_PORT/" .env
sed -i "s/FRONT_PORT=.*/FRONT_PORT=$FRONT_PORT/" .env
sed -i "s/SERVER_IP=.*/SERVER_IP=$SERVER_IP/" .env

echo "==============================="
echo " KCD Formes — Déploiement"
echo " Serveur : $SERVER_IP"
echo " Front   : http://$SERVER_IP:$FRONT_PORT"
echo " API     : http://$SERVER_IP:$API_PORT"
echo " Swagger : http://$SERVER_IP:$API_PORT/swagger-ui/index.html"
echo " DB      : port $DB_PORT"
echo "==============================="

# Arrête les anciens containers si présents
docker compose down 2>/dev/null

# Lance le projet
docker compose up -d --build

# Affiche l'état
echo ""
docker compose ps