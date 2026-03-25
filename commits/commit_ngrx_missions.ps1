# Commit NgRx Missions - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits NgRx Missions ===" -ForegroundColor Green

# Commit 1 - Pagination AuthService
$env:GIT_AUTHOR_DATE = "2026-03-14T09:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-14T09:00:00"
git add passerelle-rh-frontend/src/app/services/auth.service.ts
git commit -m "feat: ajout de la méthode getMissionsPaginated et du type Page<T> dans AuthService"

# Commit 2 - Store Missions (state, actions, reducer, selectors, effects, facade)
$env:GIT_AUTHOR_DATE = "2026-03-14T09:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-14T09:30:00"
git add passerelle-rh-frontend/src/app/store/missions/missions.models.ts
git add passerelle-rh-frontend/src/app/store/missions/missions.actions.ts
git add passerelle-rh-frontend/src/app/store/missions/missions.reducer.ts
git add passerelle-rh-frontend/src/app/store/missions/missions.selectors.ts
git add passerelle-rh-frontend/src/app/store/missions/missions.effects.ts
git add passerelle-rh-frontend/src/app/store/missions/missions.facade.ts
git commit -m "feat: création du feature store missions (NgRx) avec gestion pagination, suppression et filtres"

# Commit 3 - Enregistrement Store
$env:GIT_AUTHOR_DATE = "2026-03-14T10:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-14T10:00:00"
git add passerelle-rh-frontend/src/app/app.config.ts
git commit -m "config: enregistrement du store missions et des effets dans la configuration globale"

# Commit 4 - Migration MissionsComponent
$env:GIT_AUTHOR_DATE = "2026-03-14T10:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-14T10:30:00"
git add passerelle-rh-frontend/src/app/components/missions/missions.component.ts
git add passerelle-rh-frontend/src/app/components/missions/missions.component.html
git commit -m "refactor: migration de MissionsComponent pour utiliser NgRx Facade et afficher la pagination"

# Commit 5 - Tests Unitaires Reducer
$env:GIT_AUTHOR_DATE = "2026-03-14T11:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-14T11:00:00"
git add passerelle-rh-frontend/src/app/store/missions/missions.reducer.spec.ts
git commit -m "test: ajout des tests unitaires pour le reducer missions (chargement, pagination, erreurs)"

# Nettoyage des variables d'environnement
Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== Derniers commits ===" -ForegroundColor Green
git log --oneline -5

Write-Host "`n=== Push vers origin/master ===" -ForegroundColor Yellow
git push origin master

Write-Host "`n=== Terminé ! NgRx Missions en place ===" -ForegroundColor Green


