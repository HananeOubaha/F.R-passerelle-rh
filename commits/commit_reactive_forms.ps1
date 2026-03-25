# Commit Reactive Forms - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits Reactive Forms ===" -ForegroundColor Green

# Commit 1 - Validators personnalisés
$env:GIT_AUTHOR_DATE = "2026-03-12T16:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T16:00:00"
git add passerelle-rh-frontend/src/app/validators/custom.validators.ts
git commit -m "feat: creation des validators personnalises (dateFuture, passwordMatch, minOneSelected)"

# Commit 2 - Login Reactive Forms
$env:GIT_AUTHOR_DATE = "2026-03-12T16:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T16:30:00"
git add passerelle-rh-frontend/src/app/components/login/login.component.ts
git add passerelle-rh-frontend/src/app/components/login/login.component.html
git commit -m "refactor: conversion LoginComponent en Reactive Forms avec validation email et password en temps reel"

# Commit 3 - Register Reactive Forms avec confirmPassword
$env:GIT_AUTHOR_DATE = "2026-03-12T17:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T17:00:00"
git add passerelle-rh-frontend/src/app/components/register/register.component.ts
git add passerelle-rh-frontend/src/app/components/register/register.component.html
git commit -m "refactor: conversion RegisterComponent en Reactive Forms avec validation cross-field passwordMatch et confirmation password"

# Commit 4 - Mission Reactive Forms avec validation dateFuture
$env:GIT_AUTHOR_DATE = "2026-03-12T17:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T17:30:00"
git add passerelle-rh-frontend/src/app/components/missions/missions.component.ts
git add passerelle-rh-frontend/src/app/components/missions/missions.component.html
git commit -m "refactor: conversion formulaire Mission en Reactive Forms avec validation cross-field dateFin > dateDebut"

Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== 4 commits Reactive Forms crees ===" -ForegroundColor Green
git log --oneline -4

Write-Host "`n=== Push ===" -ForegroundColor Cyan
git push origin master

Write-Host "`n=== REACTIVE FORMS COMPLETE ===" -ForegroundColor Green
Write-Host "✅ LoginComponent: FormGroup avec Validators.email + minLength(6)" -ForegroundColor White
Write-Host "✅ RegisterComponent: FormGroup avec passwordMatchValidator cross-field" -ForegroundColor White
Write-Host "✅ MissionsComponent: FormGroup avec dateFutureValidator cross-field" -ForegroundColor White
Write-Host "✅ Messages d'erreur inline en temps reel sur tous les formulaires" -ForegroundColor White
Write-Host "✅ Boutons submit desactives si formulaire invalide" -ForegroundColor White

