#!/bin/bash
# ============================================================
# KCD Formes — Arrêt de l'API
# ============================================================

API_PORT=${1:-8081}

echo "Arrêt de KCD Formes sur le port ${API_PORT}..."

PID=$(lsof -t -i:${API_PORT} 2>/dev/null)
if [ ! -z "$PID" ]; then
    kill $PID
    echo "✓ API arrêtée (PID: $PID)"
else
    echo "✗ Aucune instance trouvée sur le port ${API_PORT}"
fi
