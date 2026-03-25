# Commit Refresh Token - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits Refresh Token ===" -ForegroundColor Green

# Commit 1 - DTO refresh request
$env:GIT_AUTHOR_DATE = "2026-03-13T15:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T15:00:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/dto/RefreshTokenRequest.java
git commit -m "feat: ajout du DTO RefreshTokenRequest pour le renouvellement de session JWT"

# Commit 2 - Backend refresh endpoint + service
$env:GIT_AUTHOR_DATE = "2026-03-13T15:20:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T15:20:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/controller/AuthController.java
git add passerelle-rh-backend/src/main/java/com/passerellerh/service/AuthService.java
git commit -m "feat: implementation du endpoint POST /api/auth/refresh avec validation du refresh token"

# Commit 3 - Tests backend refresh
$env:GIT_AUTHOR_DATE = "2026-03-13T15:35:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T15:35:00"
git add passerelle-rh-backend/src/test/java/com/passerellerh/service/AuthServiceTest.java
git commit -m "test: ajout de 2 tests unitaires AuthService pour le flux refresh token (valide/invalide)"

# Commit 4 - Frontend auth service refresh
$env:GIT_AUTHOR_DATE = "2026-03-13T15:50:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T15:50:00"
git add passerelle-rh-frontend/src/app/services/auth.service.ts
git commit -m "feat: ajout de refreshAccessToken et gestion de mise a jour atomique des tokens cote frontend"

# Commit 5 - Interceptor auto refresh on 401
$env:GIT_AUTHOR_DATE = "2026-03-13T16:10:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T16:10:00"
git add passerelle-rh-frontend/src/app/interceptors/auth.interceptor.ts
git commit -m "feat: interceptor JWT avec refresh automatique sur 401 et protection contre les boucles infinies"

Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== 5 commits Refresh Token crees ===" -ForegroundColor Green
git log --oneline -5

Write-Host "`n=== Push ===" -ForegroundColor Cyan
git push origin master

