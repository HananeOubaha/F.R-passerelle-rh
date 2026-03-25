# Passerelle RH 🌉

> **Révélez vos Talents & Validez l'Avenir**  
> Une plateforme moderne de valorisation des compétences professionnelles et de certification d'expériences.

![Passerelle RH Hero](https://via.placeholder.com/1200x600/003087/ffffff?text=Passerelle+RH+Preview)

## 📋 À Propos

**Passerelle RH** est une application web innovante conçue pour combler le fossé entre les jeunes talents et le monde professionnel. Elle permet aux utilisateurs de transformer leurs missions ponctuelles (bénévolat, stages, certifiés) en un **Passeport de Compétences** validé par des professionnels.

L'application offre un écosystème complet pour :
- **Les Candidats** : Créer des missions, demander des validations, obtenir des badges et partager un profil public certifié.
- **Les Valideurs** : Confirmer les acquis et évaluer les compétences avec un système de notation précis.
- **Les Administrateurs** : Gérer les utilisateurs et superviser l'activité de la plateforme.

## ✨ Fonctionnalités Clés

### 🎓 Pour les Candidats
- **Création de Missions** : Déclarez vos expériences professionnelles et bénévoles.
- **Passeport RH** : Visualisez l'ensemble de vos compétences validées sous forme de graphique radar.
- **Gamification** : Débloquez des badges et progressez par niveaux (Débutant, Confirmé, Expert).
- **Profil Public** : Partagez un lien unique sécurisé vers votre CV numérique certifié.
- **Export PDF** : Téléchargez votre attestation de compétences.

### ✅ Pour les Valideurs (Managers/Employeurs)
- **Tableau de Bord Dédié** : Visualisez les missions en attente de validation.
- **Évaluation Précise** : Notez chaque compétence sur une échelle de 1 à 5.
- **Feedback** : Ajoutez des commentaires constructifs pour guider le candidat.

### 🛠️ Pour les Administrateurs
- **Gestion des Utilisateurs** : Contrôle total sur les comptes et rôles.
- **Statistiques** : Suivi en temps réel de l'activité de la plateforme.

## 🚀 Architecture Technique

Le projet est divisé en deux parties distinctes (Monorepo) :

### Backend (API REST)
- **Framework** : Spring Boot 3.2.2 (Java 17/21)
- **Base de Données** : PostgreSQL (Production) / H2 (Dev)
- **Sécurité** : Spring Security + JWT (Authentication Stateless)
- **Documentation** : Swagger UI / OpenAPI
- **Build Tool** : Maven

### Frontend (SPA)
- **Framework** : Angular 17
- **Styling** : Tailwind CSS (Thème Blue/Slate Corporate)
- **Charts** : Ngx-Charts pour la visualisation des compétences.
- **Design** : Responsive, Mobile-first.

## 🛠️ Prérequis

Avant de commencer, assurez-vous d'avoir installé :
- **Java JDK 17** ou supérieur
- **Node.js 18** ou supérieur (avec NPM)
- **Docker & Docker Compose** (optionnel, pour la base de données)
- **Maven** (inclus via wrapper `mvnw`)

## 📦 Installation & Démarrage

### 1. Cloner le projet
```bash
git clone https://github.com/votre-username/passerelle-rh.git
cd passerelle-rh
```

### 2. Démarrer la Base de Données (Docker)
Si vous utilisez Docker pour PostgreSQL :
```bash
docker-compose up -d
```

### 3. Lancer le Backend (Spring Boot)
Ouvrez un terminal dans le dossier `passerelle-rh-backend` :
```bash
cd passerelle-rh-backend
# Windows
./mvnw.cmd spring-boot:run
# Linux/Mac
./mvnw spring-boot:run
```
Le serveur API démarrera sur `http://localhost:8080`.

### 4. Lancer le Frontend (Angular)
Ouvrez un **nouveau terminal** dans le dossier `passerelle-rh-frontend` :
```bash
cd passerelle-rh-frontend
npm install  # Installation des dépendances (première fois uniquement)
npm start
```
L'application sera accessible sur `http://localhost:4200`.

## 🖥️ Utilisation

### Comptes de Démonstration (si pré-configurés)
- **Admin** : `admin@passerellerh.com` / `admin123`
- **Validateur** : `validator@entreprise.com` / `valid123`
- **Utilisateur** : `user@email.com` / `user123`

### Scénario Typique
1. **Inscription** : Un utilisateur crée un compte "Candidat".
2. **Création** : Il ajoute une mission "Stage Marketing" et invite son maître de stage (email du validateur).
3. **Validation** : Le validateur reçoit la demande, se connecte, note les compétences (Communication, Analyse...) et valide.
4. **Certification** : Le candidat reçoit ses points, son niveau augmente, et la mission apparaît certifiée sur son Profil Public.

## 🎨 Identité Visuelle

L'application utilise une palette de couleurs professionnelle inspirée des outils RH modernes :
- **Primaire** : Bleu Roi (`#003087`) & Bleu Cyan (`#00A3E0`)
- **Validation** : Vert Émeraude (`#00C853`)
- **Accent** : Indigo (`#4F46E5`)
- **Fond** : Slate (`#F8FAFC`)

Le logo "Passerelle" symbolise la connexion et l'élévation professionnelle.

---

## 🤝 Contribuer

Les contributions sont les bienvenues !
1. Forkez le projet
2. Créez votre branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Pushez vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📄 Licence

Distribué sous la licence MIT. Voir `LICENSE` pour plus d'informations.

---

&copy; 2026 Passerelle RH. Développé avec passion pour l'avenir des talents.


