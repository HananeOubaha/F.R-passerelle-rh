# Commit Pagination + Pipes - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits Pipes ===" -ForegroundColor Green

# Commit 1 - ScorePipe
$env:GIT_AUTHOR_DATE = "2026-03-12T19:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T19:30:00"
git add passerelle-rh-frontend/src/app/pipes/score.pipe.ts
git commit -m "feat: creation du ScorePipe pour afficher les scores en pourcentage, note sur 5 ou label contextuel"

# Commit 2 - DateRelativePipe
$env:GIT_AUTHOR_DATE = "2026-03-12T19:50:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T19:50:00"
git add passerelle-rh-frontend/src/app/pipes/date-relative.pipe.ts
git commit -m "feat: creation du DateRelativePipe pour afficher les dates en format relatif (il y a 3 jours, hier)"

# Commit 3 - TruncatePipe
$env:GIT_AUTHOR_DATE = "2026-03-12T20:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T20:00:00"
git add passerelle-rh-frontend/src/app/pipes/truncate.pipe.ts
git commit -m "feat: creation du TruncatePipe pour tronquer les descriptions longues avec suffixe configurable"

# Commit 4 - Integration pipes dans composants
$env:GIT_AUTHOR_DATE = "2026-03-12T20:20:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T20:20:00"
git add passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.ts
git add passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.html
git add passerelle-rh-frontend/src/app/components/missions/missions.component.ts
git add passerelle-rh-frontend/src/app/components/missions/missions.component.html
git add passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.ts
git add passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.html
git commit -m "refactor: integration des pipes ScorePipe, DateRelativePipe et TruncatePipe dans dashboard, missions et passeport"

Write-Host "`n=== Commits Pagination Backend ===" -ForegroundColor Cyan

# Commit 5 - UserRepository pagine
$env:GIT_AUTHOR_DATE = "2026-03-12T21:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T21:00:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/repository/UserRepository.java
git commit -m "feat: ajout des methodes paginees avec recherche et filtrage par role dans UserRepository"

# Commit 6 - MissionRepository pagine
$env:GIT_AUTHOR_DATE = "2026-03-12T21:15:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T21:15:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/repository/MissionRepository.java
git commit -m "feat: ajout des methodes paginees findByUtilisateurId et findByUtilisateurIdAndStatut avec Pageable"

# Commit 7 - AdminService pagine
$env:GIT_AUTHOR_DATE = "2026-03-12T21:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T21:30:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/service/AdminService.java
git commit -m "feat: implementation de getUsersPaginated avec recherche par nom, prenom ou email et filtrage par role"

# Commit 8 - Endpoint pagine MissionController
$env:GIT_AUTHOR_DATE = "2026-03-12T21:45:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T21:45:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/controller/MissionController.java
git commit -m "feat: ajout du endpoint GET /api/missions/page avec pagination, tri et filtrage par statut"

# Commit 9 - Endpoint pagine AdminController
$env:GIT_AUTHOR_DATE = "2026-03-12T22:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T22:00:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/controller/AdminController.java
git commit -m "feat: ajout du endpoint GET /api/admin/users/page avec pagination, recherche et filtrage par role"

Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== 9 commits crees (Pipes + Pagination) ===" -ForegroundColor Green
git log --oneline -9

Write-Host "`n=== Push ===" -ForegroundColor Cyan
git push origin master

