# KCD Formes

**Tower Defense médiéval en formes géométriques**

Projet fil rouge — Licence Professionnelle Full Stack · UHA 4.0 (Université de Haute-Alsace, Mulhouse)

---

## Présentation

KCD Formes est un jeu de tower defense où les tours et les ennemis sont représentés par des formes géométriques. Le joueur place des tourelles (cercles, triangles, losanges) sur une carte pour défendre son territoire contre des vagues d'ennemis, avec un système d'équilibrage pierre-feuille-ciseaux entre les types de formes.

Le projet est développé dans le cadre du module PRO402, structuré en étapes progressives couvrant l'architecture backend, la logique de jeu, le temps réel via WebSocket et le déploiement Docker.

---

## Stack technique

| Couche | Technologie |
|---|---|
| Backend | Java 21 · Spring Boot 3.4 |
| ORM | Spring Data JPA / Hibernate |
| Base de données (prod) | MariaDB 11 |
| Base de données (dev) | H2 (in-memory) |
| Temps réel | WebSocket / STOMP |
| Frontend | Next.js |
| Conteneurisation | Docker · Docker Compose |
| Build | Maven 3.9 |
| Versioning | Git (GitLab UHA 4.0) |

---

## Prérequis

**Machine locale (développement) :**
- Java 21
- Maven 3.9+

**Serveur (déploiement) :**
- Docker et Docker Compose
- Accès SSH

---

## Installation & Lancement

### Développement local

```bash
# Cloner le projet
git clone ssh://git@git.uha4point0.fr:22222/UHA40/fil-rouge-2024/4.0.2-de/kim-kcd-formes.git
cd kim-kcd-formes

# Lancer avec le profil dev (H2 in-memory)
mvn spring-boot:run
```

L'API est accessible sur `http://localhost:8080`.

### Déploiement Docker (production)

Le déploiement suit une approche **JAR-only** : le code est compilé en local et seul le JAR est envoyé sur le serveur. Aucun code source n'est exposé sur le serveur.

#### 1. Build en local

```bash
mvn clean package -DskipTests
```

#### 2. Envoyer les fichiers sur le serveur

```bash
# Envoyer le JAR
scp target/kcd-form-1.0-SNAPSHOT.jar uha40@<IP_SERVEUR>:~/kcdformes/app.jar

# Envoyer les fichiers Docker
scp Dockerfile docker-compose.yml .env.example uha40@<IP_SERVEUR>:~/kcdformes/
```

#### 3. Configurer et lancer sur le serveur

```bash
ssh uha40@<IP_SERVEUR>
cd ~/kcdformes

# Créer le fichier .env
cp .env.example .env
# Adapter les valeurs si nécessaire

# Lancer les containers
docker compose up -d --build

# Vérifier
docker compose ps
docker compose logs -f api
```

L'API est accessible sur `http://<IP_SERVEUR>:8888`.

---

## Configuration

Les variables d'environnement sont externalisées dans un fichier `.env` (non versionné). Un fichier `.env.example` est fourni comme modèle :

| Variable | Description | Valeur par défaut |
|---|---|---|
| `MARIADB_ROOT_PASSWORD` | Mot de passe root MariaDB | — |
| `MARIADB_DATABASE` | Nom de la base de données | `kcdformes` |
| `MARIADB_USER` | Utilisateur MariaDB | `kcdformes` |
| `MARIADB_PASSWORD` | Mot de passe utilisateur | — |
| `SPRING_DATASOURCE_URL` | URL JDBC de connexion | `jdbc:mariadb://db:3306/kcdformes` |
| `SPRING_DATASOURCE_USERNAME` | Utilisateur Spring | `kcdformes` |
| `SPRING_DATASOURCE_PASSWORD` | Mot de passe Spring | — |
| `API_PORT` | Port exposé de l'API | `8888` |
| `DB_PORT` | Port exposé de MariaDB | `3307` |

> **Le fichier `.env` ne doit jamais être commité.** Il est inclus dans le `.gitignore`.

---

## Architecture Docker

```
[Machine locale]              [Serveur Docker]
 mvn package                   
 JAR ──── scp ────────>  Container API (port 8888)
                                │
                          réseau Docker interne
                                │
                          Container MariaDB (port 3306)
                                │
                          Volume Docker (kcdformes-data)
                          → données persistantes
```

Le Dockerfile utilise une image **runtime JRE 21** uniquement — le build Maven est fait en local, ce qui garantit qu'aucun code source n'est présent sur le serveur.

---

## Endpoints

| Service | URL |
|---|---|
| API REST | `http://<IP>:8888/api/` |
| Swagger UI | `http://<IP>:8888/swagger-ui/index.html` |

---

## Commandes utiles

| Action | Commande |
|---|---|
| Lancer les containers | `docker compose up -d --build` |
| Voir l'état | `docker compose ps` |
| Logs en temps réel | `docker compose logs -f` |
| Logs API uniquement | `docker compose logs -f api` |
| Redémarrer | `docker compose restart` |
| Tout arrêter | `docker compose down` |
| Arrêter + supprimer les données | `docker compose down -v` |
| Rebuild complet | `docker compose up -d --build --force-recreate` |
| Accès shell MariaDB | `docker exec -it kcdformes-db mariadb -u kcdformes -p` |

---

## Tests

Le projet dispose d'une suite de tests JUnit 5 avec une approche Gherkin (Given/When/Then).

```bash
# Lancer les tests
mvn test

# Lancer les tests avec couverture
mvn test jacoco:report
```

---

## Structure du projet

```
kim-kcd-formes/
├── src/
│   ├── main/
│   │   ├── java/fr/kcdform/    # Code source
│   │   └── resources/           # Configuration Spring
│   └── test/                    # Tests unitaires
├── frontend/                    # Next.js (à venir)
├── docker-compose.yml           # Orchestration des containers
├── Dockerfile                   # Runtime JRE (pas de build)
├── .env.example                 # Modèle de configuration
├── .gitignore
├── pom.xml                      # Dépendances Maven
└── README.md
```

---

## Auteur

**Kim** — Licence Professionnelle Full Stack · UHA 4.0 · 2024-2026

---

### Frontend (développement local)
```bash
cd frontend
npm install
npm run dev
```

Le front est accessible sur `http://localhost:3000`.

> L'URL de l'API est configurée via la variable d'environnement `NEXT_PUBLIC_API_URL`.  
> Crée un fichier `.env.local` à la racine de `frontend/` :
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

Pour pointer vers un serveur déployé, remplace par l'IP du serveur :
```env
NEXT_PUBLIC_API_URL=http://<IP_SERVEUR>:8888
```
```

Et dans la section **Structure du projet**, remplace la ligne `frontend/` :
```
├── frontend/                    # Application Next.js (src/app/)
│   ├── src/app/                 # Pages et composants
│   ├── .env.local               # Variables d'environnement (non versionné)
│   └── .env.local.example       # Modèle