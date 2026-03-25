# Guide CI/CD - Passerelle RH

## 🚀 État de l'implémentation

L'implémentation actuelle couvre la partie **CI (Intégration Continue)**.  
Votre pipeline GitHub Actions est configuré pour vérifier automatiquement que le code compile et que les tests passent à chaque modification.

> **Note :** La partie **CD (Déploiement Continu)** n'est pas encore présente (pas de déploiement automatique vers un serveur ou Docker Hub).

---

## 🛠️ Fonctionnement du Pipeline (`.github/workflows/ci.yml`)

Le fichier de configuration définit deux jobs qui s'exécutent en parallèle sur les serveurs de GitHub :

### 1. Build & Test Backend (`build-backend`)
*   **Environnement :** Ubuntu avec Java 17 (Temurin).
*   **Actions :**
    1.  Récupération du code (`checkout`).
    2.  Installation de Java et mise en cache des dépendances Maven.
    3.  Compilation du projet (`mvn package`).
    4.  Exécution des tests unitaires (`mvn test`).

### 2. Build Frontend (`build-frontend`)
*   **Environnement :** Ubuntu avec Node.js 20.
*   **Actions :**
    1.  Récupération du code.
    2.  Installation de Node.js et mise en cache NPM.
    3.  Installation propre des dépendances (`npm ci`).
    4.  Exécution des tests Angular (`npm test` avec Chrome Headless).
    5.  Compilation de l'application en mode production (`npm run build`).

---

## ✅ Comment tester si cela fonctionne ?

### 1. Tester Localement (Avant de pousser)
Pour être sûr que la CI passera, vous pouvez simuler les étapes sur votre machine :

**Backend :**
```powershell
cd passerelle-rh-backend
.\mvnw.cmd clean test
```
*Si vous voyez `BUILD SUCCESS`, c'est bon.*

**Frontend :**
```powershell
cd passerelle-rh-frontend
npm install
npm run test -- --watch=false --browsers=ChromeHeadless
npm run build
```
*Si les tests passent et le build se termine sans erreur, c'est bon.*

### 2. Vérifier sur GitHub (Après le push)
Une fois vos changements envoyés (`git push`), allez sur votre repository GitHub :
1.  Cliquez sur l'onglet **Actions**.
2.  Vous verrez le workflow "Passerelle RH - CI Pipeline" en cours d'exécution.
3.  Cliquez dessus pour voir le détail des étapes en temps réel.
4.  ✅ **Vert** = Tout est OK.
5.  ❌ **Rouge** = Un test a échoué ou le build a planté (cliquez pour voir les logs d'erreur).

---

## 🔧 Améliorations possibles (CD)

Pour passer au **CD (Déploiement Continu)**, il faudrait ajouter des étapes comme :
*   Construction et push des images Docker vers un registre (Docker Hub / GitHub Container Registry).
*   Déploiement automatique sur un VPS ou une plateforme Cloud (via SSH ou Kubernetes).

---

# 🚀 Guide Déploiement Continu (CD)

Le fichier `.github/workflows/cd.yml` a été ajouté pour gérer le déploiement automatique.

## ⚙️ Configuration requise (Secrets GitHub)

Pour que le CD fonctionne, vous devez ajouter les variables suivantes dans **Settings > Secrets and variables > Actions** de votre repository GitHub :

| Nom du Secret | Description |
|---|---|
| `DOCKER_USERNAME` | Votre identifiant Docker Hub |
| `DOCKER_PASSWORD` | Votre token d'accès ou mot de passe Docker Hub |
| `VPS_HOST` | Adresse IP ou domaine de votre serveur VPS |
| `VPS_USER` | Utilisateur SSH (ex: `root` ou `debian`) |
| `SSH_PRIVATE_KEY` | Clé privée SSH pour se connecter au VPS |

## ▶️ Comment déclencher le déploiement ?

Le pipeline CD est configuré pour se lancer automatiquement quand vous créez un **Release Tag** (version).

**Exemple :**
```bash
git tag v1.0.0
git push origin v1.0.0
```
Cela va :
1.  Construire les images Docker.
2.  Les pousser sur Docker Hub.
3.  Se connecter au VPS et mettre à jour les conteneurs via `docker-compose`.

Vous pouvez aussi le lancer manuellement depuis l'onglet **Actions > Passerelle RH - CD Pipeline > Run workflow**.
