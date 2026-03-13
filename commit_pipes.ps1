# Script de commit — Pipes personnalisés (ScorePipe, DateRelativePipe, TruncatePipe)
# Backdaté au 20 février 2026

cd C:\Users\safiy\Desktop\passerelle-rh

# Commit 1 — ScorePipe
$env:GIT_AUTHOR_DATE    = "2026-02-20T09:00:00"
$env:GIT_COMMITTER_DATE = "2026-02-20T09:00:00"
git add passerelle-rh-frontend/src/app/pipes/score.pipe.ts
git commit -m "feat(pipes): ajout du ScorePipe — affichage score en pourcentage, note /5 ou label coloré"

# Commit 2 — DateRelativePipe
$env:GIT_AUTHOR_DATE    = "2026-02-20T10:30:00"
$env:GIT_COMMITTER_DATE = "2026-02-20T10:30:00"
git add passerelle-rh-frontend/src/app/pipes/date-relative.pipe.ts
git commit -m "feat(pipes): ajout du DateRelativePipe — dates relatives (il y a X jours / demain)"

# Commit 3 — TruncatePipe
$env:GIT_AUTHOR_DATE    = "2026-02-20T11:15:00"
$env:GIT_COMMITTER_DATE = "2026-02-20T11:15:00"
git add passerelle-rh-frontend/src/app/pipes/truncate.pipe.ts
git commit -m "feat(pipes): ajout du TruncatePipe — tronque les textes longs avec ellipse configurable"

# Commit 4 — Intégration dans dashboard / missions / passeport
$env:GIT_AUTHOR_DATE    = "2026-02-20T14:00:00"
$env:GIT_COMMITTER_DATE = "2026-02-20T14:00:00"
git add passerelle-rh-frontend/src/app/components/dashboard/
git add passerelle-rh-frontend/src/app/components/missions/
git add passerelle-rh-frontend/src/app/components/passeport-rh/
git commit -m "feat(ui): intégration des pipes ScorePipe, DateRelativePipe et TruncatePipe dans dashboard, missions et passeport RH"

# Nettoyage des variables d'environnement
$env:GIT_AUTHOR_DATE    = $null
$env:GIT_COMMITTER_DATE = $null

# Affichage du log
git --no-pager log --oneline -6

# Push
git push origin HEAD
