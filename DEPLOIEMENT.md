# KCD Formes — Guide de déploiement

## Architecture de déploiement

```
[Serveur API]                    [Serveur BDD]
  Spring Boot (port 8081)  --->    MariaDB (port 3306)
  JAR exécutable                   Base: kcdformes
```

## Prérequis

- Java 21+ sur le serveur API
- MariaDB 10+ sur le serveur BDD
- Accès SSH aux serveurs

## Étape 1 : Générer le JAR

Sur votre machine de développement :

```bash
cd ~/IdeaProjects/kcd-form
mvn clean package -DskipTests
# Le JAR est généré dans target/kcd-form-1.0-SNAPSHOT.jar
```

## Étape 2 : Configurer la BDD

Copier le script sur le serveur BDD et l'exécuter :

```bash
scp deploy-bdd.sh user@IP_SERVEUR_BDD:~/
ssh user@IP_SERVEUR_BDD
chmod +x deploy-bdd.sh
./deploy-bdd.sh
```

Le script crée automatiquement :
- La base de données `kcdformes`
- L'utilisateur `kcdformes` avec mot de passe `kcdformes2026`
- Les droits d'accès distant si nécessaire

## Étape 3 : Déployer l'API

Copier le script et le JAR sur le serveur API :

```bash
scp deploy-api.sh target/kcd-form-1.0-SNAPSHOT.jar user@IP_SERVEUR_API:~/
ssh user@IP_SERVEUR_API
chmod +x deploy-api.sh
./deploy-api.sh
```

**Important** : Avant de lancer, modifier les variables dans le script :
- `API_PORT` : choisir un port libre (par défaut 8081)
- `DB_HOST` : IP du serveur BDD (localhost si même serveur)

## Étape 4 : Vérification

L'API est accessible à : `http://IP_SERVEUR_API:8081`
Swagger UI : `http://IP_SERVEUR_API:8081/swagger-ui/index.html`

## Changer le port de l'API

Deux méthodes possibles :

1. Modifier la variable `API_PORT` dans `deploy-api.sh` et relancer
2. Lancer manuellement avec un port différent :
```bash
java -jar kcd-form-1.0-SNAPSHOT.jar --spring.profiles.active=prod --server.port=9090
```

## Arrêter l'API

```bash
./stop-api.sh 8081
```

## Consulter les logs

```bash
tail -f /opt/kcdformes/app.log
```

## Accès à la base de données

- **URL JDBC** : `jdbc:mariadb://IP_SERVEUR_BDD:3306/kcdformes`
- **Utilisateur** : `kcdformes`
- **Mot de passe** : `kcdformes2026`
- **Console** : `mariadb -u kcdformes -pkcdformes2026 kcdformes`

## Structure de la BDD

| Table       | Description                          |
|-------------|--------------------------------------|
| formes      | Formes géométriques (Triangle, Cercle, Rectangle) |
| joueurs     | Joueurs avec budget, score, vies     |
| parties     | Parties de jeu avec état et difficulté |
| tourelles   | Tourelles placées dans une partie    |
| murailles   | Murailles (Rectangle) dans une partie |

## Données initiales

Au premier lancement, le `DataInitializer` crée automatiquement :
- 1 joueur par défaut
- 2 compositions (parties) : 1 vide + 1 avec 4 tourelles
