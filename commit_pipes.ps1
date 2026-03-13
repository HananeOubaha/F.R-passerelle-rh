# Commit Pipes personnalises - Passerelle RH
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

# Commit 4 - Integration dans Dashboard
$env:GIT_AUTHOR_DATE = "2026-03-12T20:15:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T20:15:00"
git add passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.ts
git add passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.html
git commit -m "refactor: integration du ScorePipe dans le dashboard pour remplacer les ternaires de score par le pipe"

# Commit 5 - Integration dans Missions
$env:GIT_AUTHOR_DATE = "2026-03-12T20:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T20:30:00"
git add passerelle-rh-frontend/src/app/components/missions/missions.component.ts
git add passerelle-rh-frontend/src/app/components/missions/missions.component.html
git commit -m "refactor: integration des pipes TruncatePipe et DateRelativePipe dans les cartes de mission"

# Commit 6 - Integration dans Passeport RH
$env:GIT_AUTHOR_DATE = "2026-03-12T20:45:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T20:45:00"
git add passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.ts
git add passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.html
git commit -m "refactor: integration ScorePipe et DateRelativePipe dans le passeport RH et la timeline des validations"

Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== 6 commits Pipes crees ===" -ForegroundColor Green
git log --oneline -6

Write-Host "`n=== Push ===" -ForegroundColor Cyan
git push origin master

