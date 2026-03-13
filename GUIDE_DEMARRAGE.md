# 🚀 Guide de Démarrage - Passerelle RH

Ce guide explique comment lancer l'application complète (Base de données + Backend + Frontend) selon deux méthodes :
1. **Mode Docker** (Recommandé pour la rapidité/démo)
2. **Mode Local** (Recommandé pour le développement/debug)

---

## 📋 Prérequis Généraux

- **Git** installé.
- **PowerShell** (Windows).
- Avoir cloné le projet :
  ```powershell
  cd C:\Users\safiy\Desktop\passerelle-rh
  ```

---

## 🐳 OPTION 1 : Lancer avec DOCKER (Tout automatique)

Cette méthode lance la base de données, le backend et le frontend dans des conteneurs isolés.

### 1. Préparation
Assure-toi que **Docker Desktop** est lancé.
⚠️ **Important** : Arrête ton PostgreSQL local pour libérer le port 5432.
```powershell
net stop postgresql-x64-17
```

### 2. Configuration Environnement
Le fichier `docker-compose.yml` attend des variables pour l'email. Tu peux créer un fichier `.env` à la racine ou définir les variables temporairement. Pour faire simple, Docker prendra les valeurs par défaut ou vides si non définies, mais pour éviter des erreurs au lancement :

Créer un fichier `.env` à la racine du projet (`passerelle-rh/.env`) avec ce contenu :
```env
EMAIL_USER=monemail@test.com
EMAIL_PASS=monmotdepasse
```

### 3. Lancement
Depuis la racine du projet (`passerelle-rh/`) :

```powershell
# Construire et lancer en arrière-plan
docker-compose up --build -d

# Voir les logs (pour vérifier que tout démarre)
docker-compose logs -f
```

### 4. Accès
- **Frontend** : http://localhost:4200
- **Backend API** : http://localhost:8080/api
- **Base de données** : localhost:5432 (User: `postgres`, Pass: `root`)

### 5. Arrêt
```powershell
docker-compose down
```

---

## 💻 OPTION 2 : Lancer en LOCAL (Sans Docker)

Cette méthode lance chaque brique manuellement sur ta machine Windows.

### 1. Démarrer la Base de Données (PostgreSQL)
Assure-toi que Docker est éteint (`docker-compose down`).

```powershell
# Démarrer le service Windows PostgreSQL
net start postgresql-x64-17
```

*Vérification* : Assure-toi via pgAdmin ou DBeaver que la base de données `passerelle_rh` existe. Si non, crée-la :
`CREATE DATABASE passerelle_rh;`

### 2. Démarrer le Backend (Spring Boot)
Ouvre un **nouveau** terminal PowerShell.

```powershell
message "Lancement du Backend..."
cd passerelle-rh-backend

# Définir les variables d'environnement (optionnel si tu as mis des valeurs par défaut dans le code)
$env:EMAIL_USER="test@gmail.com"
$env:EMAIL_PASS="password123"

# Lancer le serveur (Maven Wrapper)
.\mvnw.cmd spring-boot:run
```
*Attendre de voir : `Started PasserelleRhApplication in X seconds`*

### 3. Démarrer le Frontend (Angular)
Ouvre un **nouveau** terminal PowerShell.

```powershell
message "Lancement du Frontend..."
cd passerelle-rh-frontend

# Installer les dépendances (à faire une seule fois)
npm install

# Lancer le serveur de développement
npm start
```
*Attendre de voir : `Application bundle generation complete.`*

### 4. Accès
- **Frontend** : http://localhost:4200
- **Backend** : http://localhost:8080

---

## ❓ Résolution de problèmes fréquents

### Port 5432 déjà utilisé ?
Si tu as une erreur `Bind for 0.0.0.0:5432 failed: port is already allocated` :
- Soit Docker tourne encore -> `docker-compose down`
- Soit PostgreSQL local tourne -> `net stop postgresql-x64-17`

### Erreur de connexion DB "Bad credentials" ?
Vérifie `src/main/resources/application.yml`.
Par défaut : `user: postgres`, `password: root`.
Si ton mot de passe local Windows est différent, change-le dans ce fichier.

### Erreur CORS ?
Vérifie que le Backend tourne bien sur le port 8080.
Le Frontend essaie de taper sur `http://localhost:8080/api/...`.

