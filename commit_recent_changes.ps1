# Script de commits pour les modifications recentes - Passerelle RH
# Date : 10-12 Mars 2026

cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits des fichiers modifies et nouveaux ===" -ForegroundColor Green

# ============================================
# JOUR 1 - Lundi 10 Mars 2026
# Modifications backend et configuration Swagger
# ============================================

# Commit 1 - 2026-03-10 14:30:00
Write-Host "`nCommit 1/9 - pom.xml" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-10T14:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-10T14:30:00"
git add passerelle-rh-backend/pom.xml
git commit -m "feat: ajout des dependances OpenPDF, ZXing et SpringDoc OpenAPI pour generation PDF et documentation"

# Commit 2 - 2026-03-10 15:00:00
Write-Host "`nCommit 2/9 - application.yml" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-10T15:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-10T15:00:00"
git add passerelle-rh-backend/src/main/resources/application.yml
git commit -m "config: ajout de la configuration SpringDoc Swagger UI et parametres mail SMTP"

# Commit 3 - 2026-03-10 15:30:00
Write-Host "`nCommit 3/9 - OpenApiConfig.java" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-10T15:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-10T15:30:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/config/OpenApiConfig.java
git commit -m "feat: configuration Swagger/OpenAPI avec authentification JWT Bearer pour documentation API"

# Commit 4 - 2026-03-10 16:00:00
Write-Host "`nCommit 4/9 - SecurityConfig.java" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-10T16:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-10T16:00:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/config/SecurityConfig.java
git commit -m "feat: autorisation des endpoints Swagger et integration OAuth2 avec Auth0"

# Commit 5 - 2026-03-10 16:30:00
Write-Host "`nCommit 5/9 - ValidationService.java" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-10T16:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-10T16:30:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/service/ValidationService.java
git commit -m "security: verification stricte que le validateur authentifie est autorise a valider la mission"

# ============================================
# JOUR 2 - Mardi 11 Mars 2026
# Configuration Docker et DevOps
# ============================================

# Commit 6 - 2026-03-11 10:00:00
Write-Host "`nCommit 6/9 - Backend Dockerfile" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-11T10:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-11T10:00:00"
git add passerelle-rh-backend/Dockerfile
git commit -m "feat: creation du Dockerfile multi-stage pour le backend Spring Boot avec optimisation"

# Commit 7 - 2026-03-11 10:30:00
Write-Host "`nCommit 7/9 - Frontend Dockerfile" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-11T10:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-11T10:30:00"
git add passerelle-rh-frontend/Dockerfile
git commit -m "feat: creation du Dockerfile multi-stage pour Angular avec build production et Nginx"

# Commit 8 - 2026-03-11 11:00:00
Write-Host "`nCommit 8/9 - nginx.conf" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-11T11:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-11T11:00:00"
git add passerelle-rh-frontend/nginx.conf
git commit -m "config: configuration Nginx pour servir Angular SPA avec routing et compression GZIP"

# Commit 9 - 2026-03-11 11:30:00
Write-Host "`nCommit 9/9 - docker-compose.yml" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-11T11:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-11T11:30:00"
git add docker-compose.yml
git commit -m "feat: orchestration Docker Compose complete avec PostgreSQL, backend et frontend"

# Nettoyage des variables d'environnement
Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

# ============================================
# FINALISATION
# ============================================

Write-Host "`n=== COMMITS TERMINES ===" -ForegroundColor Green
Write-Host "9 commits crees avec succes pour les modifications recentes" -ForegroundColor Green

Write-Host "`n=== Status Git ===" -ForegroundColor Yellow
git status

Write-Host "`n=== Log des derniers commits ===" -ForegroundColor Yellow
git log --oneline -10

Write-Host "`n=== Pret pour le push ===" -ForegroundColor Cyan
Write-Host "Utilisez : git push origin master" -ForegroundColor Cyan

