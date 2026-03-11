#!/bin/bash
# ============================================================
# KCD Formes — Script de déploiement de l'API via Git
# À exécuter sur le serveur qui hébergera l'API Spring Boot
# ============================================================

# === VARIABLES À MODIFIER ===
GIT_REPO="ssh://git@git.uha4point0.fr:22222/UHA40/fil-rouge-2024/4.0.2-de/kim-kcd-formes.git"

# Port de l'API (choisir un port libre)
API_PORT=8081

# Configuration BDD (adapter l'IP si BDD sur un autre serveur)
DB_HOST="localhost"
DB_PORT=3306
DB_NAME="kcdformes"
DB_USER="kcdformes"
DB_PASSWORD="kcdformes2026"

# Répertoire de déploiement
DEPLOY_DIR="/opt/kcdformes"

echo "=========================================="
echo "  KCD Formes — Déploiement API via Git"
echo "=========================================="

# 0. Demander la branche
echo ""
read -p "Quelle branche déployer ? (défaut: main) : " GIT_BRANCH
GIT_BRANCH=${GIT_BRANCH:-main}
echo "  → Branche sélectionnée : ${GIT_BRANCH}"

# 1. Vérifier Java
echo ""
echo "[1/7] Vérification de Java..."
if command -v java &> /dev/null; then
    echo "  ✓ Java trouvé : $(java -version 2>&1 | head -1)"
else
    echo "  ✗ Java non trouvé. Installation..."
    sudo apt update && sudo apt install -y openjdk-21-jre-headless openjdk-21-jdk-headless
    echo "  ✓ Java 21 installé"
fi

# 2. Vérifier Maven
echo ""
echo "[2/7] Vérification de Maven..."
if command -v mvn &> /dev/null; then
    echo "  ✓ Maven trouvé : $(mvn -version 2>&1 | head -1)"
else
    echo "  ✗ Maven non trouvé. Installation..."
    sudo apt update && sudo apt install -y maven
    echo "  ✓ Maven installé"
fi

# 3. Vérifier Git
echo ""
echo "[3/7] Vérification de Git..."
if command -v git &> /dev/null; then
    echo "  ✓ Git trouvé"
else
    echo "  ✗ Git non trouvé. Installation..."
    sudo apt update && sudo apt install -y git
    echo "  ✓ Git installé"
fi

# 4. Cloner ou mettre à jour le repo
echo ""
echo "[4/7] Récupération du code source..."
sudo mkdir -p ${DEPLOY_DIR}
sudo chown $USER:$USER ${DEPLOY_DIR}

if [ -d "${DEPLOY_DIR}/kcd-form" ]; then
    echo "  → Repo existant, mise à jour..."
    cd ${DEPLOY_DIR}/kcd-form
    git fetch --all
    git checkout ${GIT_BRANCH}
    git pull origin ${GIT_BRANCH}
    echo "  ✓ Code mis à jour (branche: ${GIT_BRANCH})"
else
    echo "  → Clonage du repo..."
    cd ${DEPLOY_DIR}
    git clone -b ${GIT_BRANCH} ${GIT_REPO} kcd-form
    echo "  ✓ Repo cloné (branche: ${GIT_BRANCH})"
fi

# 5. Build du JAR
echo ""
echo "[5/7] Build du projet..."
cd ${DEPLOY_DIR}/kcd-form
mvn clean package -DskipTests -q

if [ $? -eq 0 ]; then
    echo "  ✓ Build réussi"
else
    echo "  ✗ Erreur de build ! Vérifiez les logs Maven"
    exit 1
fi

# 6. Arrêter l'ancienne instance et lancer la nouvelle
echo ""
echo "[6/7] Lancement de l'API..."
PID=$(lsof -t -i:${API_PORT} 2>/dev/null)
if [ ! -z "$PID" ]; then
    kill $PID
    sleep 2
    echo "  ✓ Ancienne instance arrêtée (PID: $PID)"
fi

JAR_PATH=$(find ${DEPLOY_DIR}/kcd-form/target -name "*.jar" ! -name "*sources*" | head -1)

nohup java -jar ${JAR_PATH} \
    --spring.profiles.active=prod \
    --server.port=${API_PORT} \
    --spring.datasource.url=jdbc:mariadb://${DB_HOST}:${DB_PORT}/${DB_NAME} \
    --spring.datasource.username=${DB_USER} \
    --spring.datasource.password=${DB_PASSWORD} \
    > ${DEPLOY_DIR}/app.log 2>&1 &

NEW_PID=$!
echo "  ✓ API lancée (PID: $NEW_PID)"

# 7. Vérification
echo ""
echo "[7/7] Vérification du démarrage (15s)..."
sleep 15

if curl -s http://localhost:${API_PORT}/swagger-ui/index.html > /dev/null 2>&1; then
    echo "  ✓ API accessible !"
    echo ""
    echo "=========================================="
    echo "  ✓ Déploiement réussi !"
    echo "  Branche : ${GIT_BRANCH}"
    echo "  API     : http://$(hostname -I | awk '{print $1}'):${API_PORT}"
    echo "  Swagger : http://$(hostname -I | awk '{print $1}'):${API_PORT}/swagger-ui/index.html"
    echo "  Logs    : ${DEPLOY_DIR}/app.log"
    echo "  PID     : ${NEW_PID}"
    echo "=========================================="
else
    echo "  ⚠ L'API ne répond pas encore. Vérifiez les logs :"
    echo "    tail -f ${DEPLOY_DIR}/app.log"
fi
