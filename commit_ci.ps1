# Commit CI/CD - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits CI/CD ===" -ForegroundColor Green

# Commit 1 - Amelioration Workflow
$env:GIT_AUTHOR_DATE = "2026-03-15T10:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-15T10:00:00"
git add .github/workflows/ci.yml
git commit -m "ci: renforcement du pipeline GitHub Actions (tests backend bloquants + ajout tests frontend)"

# Nettoyage des variables d'environnement
Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== Dernier commit CI ===" -ForegroundColor Green
git log --oneline -1

Write-Host "`n=== Push vers origin/master ===" -ForegroundColor Yellow
git push origin master

Write-Host "`n=== Termine ! CI/CD a jour ===" -ForegroundColor Green

