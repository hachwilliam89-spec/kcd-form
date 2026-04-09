# KCD Formes

**Tower Defense médiéval en pixel art · Formes géométriques**

Projet fil rouge — Licence Professionnelle Full Stack · UHA 4.0 (Université de Haute-Alsace, Mulhouse)

---

## Présentation

KCD Formes est un jeu de tower defense médiéval en pixel art où la géométrie dicte la mécanique de jeu : l'**aire** d'une forme détermine ses dégâts et ses points de vie, son **périmètre** définit sa portée et sa vitesse. Le joueur place des tourelles composées de formes géométriques sur une carte pour défendre sa forteresse contre des vagues d'ennemis (infanterie, cavalerie, béliers).

Le jeu propose un **mode multijoueur asymétrique** en temps réel via WebSocket : un joueur défend (placement de tourelles), l'autre attaque (composition et envoi de vagues).

Le projet est développé dans le cadre du module PRO402, structuré en étapes progressives couvrant la POO, les design patterns, l'architecture backend, le temps réel et le déploiement Docker.

---

## Gameplay

- **Tourelles** : composées de formes géométriques (cercles, triangles, losanges) — nommées automatiquement selon leur position
- **Ennemis** : Infanterie, Cavalerie, Béliers — chaque type a ses propres caractéristiques et animations spritesheet
- **Vagues** : organisées en escouades, avec difficulté progressive
- **Niveaux de difficulté** : Écuyer · Chevalier · Seigneur
- **Équilibrage** : système pierre-feuille-ciseaux entre types de formes

---

## Design Patterns

| Pattern | Utilisation |
|---|---|
| **Factory Method** | `EnnemiFactory` (interface) → `CavalierFactory`, `InfanterieFactory`, `BelierFactory` — instanciation des ennemis via `EnnemiFactoryRegistry` (injection Spring `@Component`), remplaçant les switch/case |
| **Composite** | `ComposantCombat` (interface) → `Vague` → `Escouade` → `Ennemi` — hiérarchie de combat permettant de traiter un groupe ou un individu de manière uniforme |

---

## Stack technique

| Couche | Technologie |
|---|---|
| Backend | Java 21 · Spring Boot 3.4 |
| ORM | Spring Data JPA / Hibernate |
| Base de données | MariaDB 11 |
| Temps réel | WebSocket / STOMP (`@stomp/stompjs` · `sockjs-client`) |
| Frontend | Next.js 14 · TypeScript · React |
| Style | CSS pixel art · Police Press Start 2P |
| Conteneurisation | Docker · Docker Compose |
| Build backend | Maven 3.9 |
| Versioning | Git (GitLab UHA 4.0 + GitHub) |

---

## Prérequis

**Développement local :**
- Java 21
- Maven 3.9+
- Node.js 18+ / npm

**Déploiement serveur :**
- Docker et Docker Compose
- Accès SSH

---

## Installation & Lancement

### Développement local

#### Backend

```bash
# Cloner le projet
git clone ssh://git@git.uha4point0.fr:22222/UHA40/fil-rouge-2024/4.0.2-de/kim-kcd-formes.git
cd kim-kcd-formes

# Lancer avec le profil dev (H2 in-memory)
mvn spring-boot:run
```

L'API est accessible sur `http://localhost:8080`.

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

Le front est accessible sur `http://localhost:3000`.

> L'URL de l'API est configurée via `NEXT_PUBLIC_API_URL`.
> Créer un fichier `.env.local` à la racine de `frontend/` :
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Déploiement Docker (serveurs école)

Le déploiement utilise le script `deploykcd.sh` qui automatise l'ensemble du processus :
- Détection automatique de l'IP du serveur via `hostname -I`
- Recherche de ports libres via `ss -tlnp`
- Mise à jour du `.env` via `sed`
- Build et lancement des containers

#### Envoi des fichiers sur le serveur

```bash
# Synchroniser le projet (sans node_modules)
rsync -avz --exclude='node_modules' --exclude='.git' . uha40@<IP_SERVEUR>:~/kcdformes/
```

> **Ne jamais envoyer `node_modules` via `scp` ou `rsync`** — les dépendances sont installées dans le container Docker.

#### Lancement sur le serveur

```bash
ssh uha40@<IP_SERVEUR>
cd ~/kcdformes
chmod +x deploykcd.sh
./deploykcd.sh
```

Le script lance 3 containers : **API** (Spring Boot), **DB** (MariaDB) et **Frontend** (Next.js).

> **Note** : `NEXT_PUBLIC_API_URL` est un **build ARG Docker** (intégré au bundle JS statique à la compilation), pas une simple variable d'environnement runtime.

---

## Configuration

Les variables d'environnement sont dans un fichier `.env` (non versionné). Un `.env.example` est fourni :

| Variable | Description | Valeur par défaut |
|---|---|---|
| `SERVER_IP` | IP du serveur (auto-détectée par `deploykcd.sh`) | — |
| `NEXT_PUBLIC_API_URL` | URL de l'API pour le frontend (build ARG) | — |
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
[Machine locale]                     [Serveur Docker]

rsync (sans node_modules)
     ──────────────────────>   Container Frontend (Next.js)
                                      │
                               réseau Docker interne
                                      │
                               Container API (Spring Boot, port 8888)
                                      │
                               réseau Docker interne
                                      │
                               Container MariaDB (port 3306)
                                      │
                               Volume Docker (kcdformes-data)
                               → données persistantes
```

Le container MariaDB utilise un **healthcheck** (`healthcheck.sh`) pour garantir que la base est prête avant le démarrage de l'API.

---

## Architecture WebSocket (Multijoueur)

Le mode multijoueur utilise **STOMP over WebSocket** pour la communication temps réel :

- Le serveur envoie l'état du jeu mis à jour chaque seconde sur un topic STOMP
- Le frontend traduit les positions (numéros de cases) en coordonnées pixel via `CHEMIN_POSITIONS`
- Les sprites ennemis sont animés via spritesheets (48×48, 150ms/frame)
- Le résultat de combat (`GAGNE`/`PERDU`) est envoyé du point de vue du défenseur — le frontend adapte l'affichage selon le rôle (`?role=ATTAQUANT` ou `?role=DEFENSEUR`)

---

## Endpoints

| Service | URL |
|---|---|
| API REST | `http://<IP>:8888/api/` |
| Swagger UI | `http://<IP>:8888/swagger-ui/index.html` |
| Frontend | `http://<IP>:3000` |
| WebSocket | `ws://<IP>:8888/ws` |

---

## Tests

Le projet dispose d'une suite de tests JUnit 5 avec une approche Gherkin (Given/When/Then), couvrant **97% du code**.

```bash
# Lancer les tests
mvn test

# Lancer les tests avec rapport de couverture
mvn test jacoco:report
```

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

## Structure du projet

```
kim-kcd-formes/
├── src/
│   ├── main/
│   │   ├── java/fr/kcdform/         # Code source backend
│   │   │   ├── model/               # Entités (Forme, Tourelle, Ennemi, Vague, Escouade…)
│   │   │   ├── factory/             # Factory Method (EnnemiFactory, Registry)
│   │   │   ├── service/             # Logique métier (CombatService, LobbyService…)
│   │   │   ├── controller/          # REST + WebSocket controllers
│   │   │   └── dto/                 # LobbyDTO, VagueConfigDTO…
│   │   └── resources/               # Configuration Spring
│   └── test/                        # Tests unitaires (JUnit 5)
├── frontend/                        # Application Next.js 14 (TypeScript)
│   ├── src/app/                     # Pages et composants
│   ├── public/sprites/              # Spritesheets ennemis (pixel art)
│   ├── .env.local                   # Variables d'environnement (non versionné)
│   └── .env.local.example           # Modèle
├── docker-compose.yml               # Orchestration 3 containers
├── Dockerfile                       # Build API
├── deploykcd.sh                     # Script de déploiement automatisé
├── .env.example                     # Modèle de configuration
├── .gitignore
├── pom.xml                          # Dépendances Maven
└── README.md
```

---

## Dépôts Git

| Plateforme | URL |
|---|---|
| GitLab UHA 4.0 | `git.uha4point0.fr/UHA40/fil-rouge-2024/4.0.2-de/kim-kcd-formes` |
| GitHub | `github.com/hachwilliam89-spec/kcd-form` |

---

## Auteurs

**Kim** · **William Hach**

Licence Professionnelle Full Stack · UHA 4.0 · 2024–2026