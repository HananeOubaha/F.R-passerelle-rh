# Commit Resolvers - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits Resolvers ===" -ForegroundColor Green

# Commit 1 - profileResolver
$env:GIT_AUTHOR_DATE = "2026-03-12T18:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T18:00:00"
git add passerelle-rh-frontend/src/app/resolvers/profile.resolver.ts
git commit -m "feat: creation du profileResolver pour pre-charger le profil utilisateur avant navigation"

# Commit 2 - missionsResolver
$env:GIT_AUTHOR_DATE = "2026-03-12T18:20:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T18:20:00"
git add passerelle-rh-frontend/src/app/resolvers/missions.resolver.ts
git commit -m "feat: creation du missionsResolver pour pre-charger les missions avant affichage dashboard"

# Commit 3 - adminStatsResolver
$env:GIT_AUTHOR_DATE = "2026-03-12T18:35:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T18:35:00"
git add passerelle-rh-frontend/src/app/resolvers/admin-stats.resolver.ts
git commit -m "feat: creation du adminStatsResolver pour pre-charger les statistiques admin"

# Commit 4 - Branchement dans les routes
$env:GIT_AUTHOR_DATE = "2026-03-12T18:50:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T18:50:00"
git add passerelle-rh-frontend/src/app/app.routes.ts
git commit -m "feat: integration des resolvers dans les routes dashboard, passeport et admin"

# Commit 5 - Adaptation des composants
$env:GIT_AUTHOR_DATE = "2026-03-12T19:10:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T19:10:00"
git add passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.ts
git add passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.ts
git add passerelle-rh-frontend/src/app/components/admin-dashboard/admin-dashboard.component.ts
git commit -m "refactor: adaptation des composants pour consommer les donnees pre-chargees par les resolvers"

Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== 5 commits Resolvers crees ===" -ForegroundColor Green
git log --oneline -5

Write-Host "`n=== Push ===" -ForegroundColor Cyan
git push origin master

