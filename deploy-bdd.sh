#!/bin/bash
# ============================================================
# KCD Formes — Script de configuration du serveur BDD
# À exécuter sur le serveur qui hébergera MariaDB
# ============================================================

# === VARIABLES À MODIFIER ===
DB_NAME="kcdformes"
DB_USER="kcdformes"
DB_PASSWORD="kcdformes2026"
# IP du serveur API (pour autoriser la connexion distante)
API_SERVER_IP="%" # "%" = toute IP, ou mettre l'IP exacte du serveur API

echo "=========================================="
echo "  KCD Formes — Configuration BDD MariaDB"
echo "=========================================="

# 1. Vérifier que MariaDB tourne
echo ""
echo "[1/4] Vérification de MariaDB..."
if systemctl is-active --quiet mariadb; then
    echo "  ✓ MariaDB est actif"
else
    echo "  ✗ MariaDB n'est pas démarré. Démarrage..."
    sudo systemctl start mariadb
    sudo systemctl enable mariadb
    echo "  ✓ MariaDB démarré et activé au boot"
fi

# 2. Créer la base de données et l'utilisateur
echo ""
echo "[2/4] Création de la base de données et de l'utilisateur..."
sudo mariadb -e "
CREATE DATABASE IF NOT EXISTS ${DB_NAME};
CREATE USER IF NOT EXISTS '${DB_USER}'@'${API_SERVER_IP}' IDENTIFIED BY '${DB_PASSWORD}';
GRANT ALL PRIVILEGES ON ${DB_NAME}.* TO '${DB_USER}'@'${API_SERVER_IP}';
FLUSH PRIVILEGES;
"

if [ $? -eq 0 ]; then
    echo "  ✓ Base '${DB_NAME}' et utilisateur '${DB_USER}' créés"
else
    echo "  ✗ Erreur lors de la création"
    exit 1
fi

# 3. Autoriser les connexions distantes (si serveur BDD ≠ serveur API)
echo ""
echo "[3/4] Configuration des connexions distantes..."
MARIADB_CONF="/etc/mysql/mariadb.conf.d/50-server.cnf"
if [ ! -f "$MARIADB_CONF" ]; then
    MARIADB_CONF="/etc/my.cnf.d/server.cnf"
fi
if [ ! -f "$MARIADB_CONF" ]; then
    MARIADB_CONF="/etc/mysql/my.cnf"
fi

if [ -f "$MARIADB_CONF" ]; then
    if grep -q "bind-address" "$MARIADB_CONF"; then
        sudo sed -i 's/^bind-address\s*=.*/bind-address = 0.0.0.0/' "$MARIADB_CONF"
        echo "  ✓ bind-address mis à 0.0.0.0"
    else
        echo "  ⚠ bind-address non trouvé dans $MARIADB_CONF (peut-être déjà configuré)"
    fi
    sudo systemctl restart mariadb
    echo "  ✓ MariaDB redémarré"
else
    echo "  ⚠ Fichier de config MariaDB non trouvé, vérifiez manuellement"
fi

# 4. Vérification
echo ""
echo "[4/4] Vérification de la connexion..."
mariadb -u ${DB_USER} -p${DB_PASSWORD} ${DB_NAME} -e "SELECT 'Connexion OK' AS status;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "  ✓ BDD prête !"
    echo "  Base     : ${DB_NAME}"
    echo "  User     : ${DB_USER}"
    echo "  Port     : 3306"
    echo "  IP       : $(hostname -I | awk '{print $1}')"
    echo "=========================================="
else
    echo "  ✗ Erreur de connexion, vérifiez le mot de passe"
fi
