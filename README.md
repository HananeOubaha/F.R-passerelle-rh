# Passerelle RH

Ce projet est composé de deux parties :
1. **Backend** : Spring Boot
2. **Frontend** : Angular 17

## Prérequis
- Java 17+
- Node.js 18+
- PostgreSQL

## Démarrage

### 1. Backend (Spring Boot)
Ouvrez un terminal dans le dossier `passerelle-rh-backend` :
```bash
cd passerelle-rh-backend
./mvnw spring-boot:run
```
Le serveur démarrera sur [http://localhost:8080](http://localhost:8080).

### 2. Frontend (Angular)
Ouvrez un *nouveau* terminal dans le dossier `passerelle-rh-frontend` :
```bash
cd passerelle-rh-frontend
npm install  # (seulement la première fois)
npm start
```
L'application sera accessible sur [http://localhost:4200](http://localhost:4200).

## Fonctionnalités Disponibles
- Inscription (`/register`)
- Connexion (`/login`)
- Dashboard sécurisé (après connexion)
