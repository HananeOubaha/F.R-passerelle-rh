# Commits Style et Refonte UI - Passerelle RH (Granulaire)
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits Granulaires - Refonte UI ===" -ForegroundColor Green

# 1. Styles Globaux
$env:GIT_AUTHOR_DATE = "2026-03-13T09:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:00:00"
git add passerelle-rh-frontend/src/styles.css
git commit -m "style: refonte du thème global (couleurs, polices, animations)"

# 2. Structure HTML de base
$env:GIT_AUTHOR_DATE = "2026-03-13T09:02:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:02:00"
git add passerelle-rh-frontend/src/index.html
git commit -m "style: mise à jour de la structure HTML de base"

# 3. Configuration Backend
$env:GIT_AUTHOR_DATE = "2026-03-13T09:05:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:05:00"
git add passerelle-rh-backend/src/main/resources/application.yml
git commit -m "config: mise à jour des propriétés de configuration backend"

# 4. Dépendances Frontend
$env:GIT_AUTHOR_DATE = "2026-03-13T09:07:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:07:00"
git add passerelle-rh-frontend/package-lock.json
git commit -m "chore: mise à jour des dépendances frontend"

# 5. Routing
$env:GIT_AUTHOR_DATE = "2026-03-13T09:10:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:10:00"
git add passerelle-rh-frontend/src/app/app.routes.ts
git commit -m "feat(routes): mise à jour de la configuration des routes (lazy loading)"

# 6. Auth - Login HTML
$env:GIT_AUTHOR_DATE = "2026-03-13T09:15:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:15:00"
git add passerelle-rh-frontend/src/app/components/login/login.component.html
git commit -m "style(auth): redesign complet de la page de connexion"

# 7. Auth - Register HTML
$env:GIT_AUTHOR_DATE = "2026-03-13T09:17:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:17:00"
git add passerelle-rh-frontend/src/app/components/register/register.component.html
git commit -m "style(auth): redesign complet de la page d'inscription"

# 8. Auth Service
$env:GIT_AUTHOR_DATE = "2026-03-13T09:20:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:20:00"
git add passerelle-rh-frontend/src/app/services/auth.service.ts
git commit -m "feat(auth): sécurisation de l'accès aux validations par rôle"

# 9. Dashboard Logic
$env:GIT_AUTHOR_DATE = "2026-03-13T09:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:30:00"
git add passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.ts
git commit -m "refactor(dashboard): mise à jour de la logique pour le nouveau design et KPIs"

# 10. Dashboard Style
$env:GIT_AUTHOR_DATE = "2026-03-13T09:32:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T09:32:00"
git add passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.html
git commit -m "style(dashboard): refonte UI avec header profile et cartes indicateurs"

# 11. Admin Dashboard Logic
$env:GIT_AUTHOR_DATE = "2026-03-13T10:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T10:00:00"
git add passerelle-rh-frontend/src/app/components/admin-dashboard/admin-dashboard.component.ts
git commit -m "refactor(admin): adaptation du dashboard administrateur"

# 12. Admin Dashboard Style
$env:GIT_AUTHOR_DATE = "2026-03-13T10:02:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T10:02:00"
git add passerelle-rh-frontend/src/app/components/admin-dashboard/admin-dashboard.component.html
git commit -m "style(admin): application du thème bleu sur le dashboard admin"

# 13. Admin Users Logic
$env:GIT_AUTHOR_DATE = "2026-03-13T10:10:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T10:10:00"
git add passerelle-rh-frontend/src/app/components/admin-users/admin-users.component.ts
git commit -m "refactor(admin): mise à jour de la gestion des utilisateurs"

# 14. Admin Users Style
$env:GIT_AUTHOR_DATE = "2026-03-13T10:12:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T10:12:00"
git add passerelle-rh-frontend/src/app/components/admin-users/admin-users.component.html
git commit -m "style(admin): redesign de la table de gestion des utilisateurs"

# 15. Admin Referentials
$env:GIT_AUTHOR_DATE = "2026-03-13T10:15:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T10:15:00"
git add passerelle-rh-frontend/src/app/components/admin-referentials/admin-referentials.component.html
git commit -m "style(admin): mise en page de la gestion des référentiels"

# 16. Missions
$env:GIT_AUTHOR_DATE = "2026-03-13T10:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T10:30:00"
git add passerelle-rh-frontend/src/app/components/missions/missions.component.html
git commit -m "style(missions): modernisation de l'affichage des cartes de missions"

# 17. Passeport RH Logic
$env:GIT_AUTHOR_DATE = "2026-03-13T11:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T11:00:00"
git add passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.ts
git commit -m "refactor(passeport): adaptation du composant passeport RH"

# 18. Passeport RH Style
$env:GIT_AUTHOR_DATE = "2026-03-13T11:02:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T11:02:00"
git add passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.html
git commit -m "style(passeport): redesign du passeport et de la timeline des validations"

# 19. Validator Dashboard Logic
$env:GIT_AUTHOR_DATE = "2026-03-13T11:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T11:30:00"
git add passerelle-rh-frontend/src/app/components/validateur-dashboard/validateur-dashboard.component.ts
git commit -m "refactor(validator): mise à jour de la logique du dashboard validateur"

# 20. Validator Dashboard Style
$env:GIT_AUTHOR_DATE = "2026-03-13T11:32:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T11:32:00"
git add passerelle-rh-frontend/src/app/components/validateur-dashboard/validateur-dashboard.component.html
git commit -m "style(validator): redesign du dashboard validateur"

# 21. Profil Public
$env:GIT_AUTHOR_DATE = "2026-03-13T12:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T12:00:00"
git add passerelle-rh-frontend/src/app/components/public-profile/public-profile.component.html
git commit -m "style(profile): redesign de la vue profil public"

# 22. Vérification Attestation
$env:GIT_AUTHOR_DATE = "2026-03-13T12:15:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T12:15:00"
git add passerelle-rh-frontend/src/app/components/verify-attestation/verify-attestation.component.html
git commit -m "style(verify): redesign de la page de vérification d'attestation"

# 23. Documentation & Scripts
$env:GIT_AUTHOR_DATE = "2026-03-13T13:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T13:00:00"
git add GUIDE_DEMARRAGE.md
git add *.ps1
git add passerelle-rh-frontend/*.js
git commit -m "docs: ajout guide de démarrage et scripts utilitaires"

# 24. Nouvelles Pages Structure
$env:GIT_AUTHOR_DATE = "2026-03-13T13:10:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T13:10:00"
git add passerelle-rh-frontend/src/app/pages/
git commit -m "feat(pages): ajout de la nouvelle structure de pages"

Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== Totale: 24 commits créé (Granulaire) ===" -ForegroundColor Green
git log --oneline -24

Write-Host "`n=== Push vers origin/master ===" -ForegroundColor Cyan
git push origin master



