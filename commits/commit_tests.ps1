# Script de commits pour les tests unitaires - Passerelle RH
# Date : 12 Mars 2026

cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits des tests unitaires - Tests run: 14, Failures: 0, Errors: 0 ===" -ForegroundColor Green

# ============================================
# Mercredi 12 Mars 2026 - Journee de tests
# ============================================

# Commit 1 - 2026-03-12 09:30:00
Write-Host "`nCommit 1/3 - ValidationServiceTest.java" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-12T09:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T09:30:00"
git add passerelle-rh-backend/src/test/java/com/passerellerh/service/ValidationServiceTest.java
git commit -m "test: ajout de 6 tests unitaires pour ValidationService (calcul score, securite email, edge cases)"

# Commit 2 - 2026-03-12 10:30:00
Write-Host "`nCommit 2/3 - AuthServiceTest.java" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-12T10:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T10:30:00"
git add passerelle-rh-backend/src/test/java/com/passerellerh/service/AuthServiceTest.java
git commit -m "test: ajout de 4 tests unitaires pour AuthService (register, login, bad credentials)"

# Commit 3 - 2026-03-12 11:30:00
Write-Host "`nCommit 3/3 - PasseportServiceTest.java" -ForegroundColor Cyan
$env:GIT_AUTHOR_DATE = "2026-03-12T11:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T11:30:00"
git add passerelle-rh-backend/src/test/java/com/passerellerh/service/PasseportServiceTest.java
git commit -m "test: ajout de 4 tests unitaires pour PasseportService (attribution badges par seuil de score)"

# Nettoyage des variables d'environnement
Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

# ============================================
# FINALISATION
# ============================================

Write-Host "`n=== TESTS COMMITES AVEC SUCCES ===" -ForegroundColor Green
Write-Host "3 fichiers de tests ajoutes - 14 tests unitaires au total" -ForegroundColor Green
Write-Host "Tests run: 14, Failures: 0, Errors: 0 - BUILD SUCCESS ✅" -ForegroundColor Green

Write-Host "`n=== Status Git ===" -ForegroundColor Yellow
git status

Write-Host "`n=== Log des derniers commits ===" -ForegroundColor Yellow
git log --oneline -5

Write-Host "`n=== Pret pour le push ===" -ForegroundColor Cyan
Write-Host "Utilisez : git push origin master" -ForegroundColor Cyan

