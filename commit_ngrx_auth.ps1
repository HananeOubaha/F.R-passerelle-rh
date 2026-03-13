# Commit NgRx Auth/Session + tests - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

Write-Host "`n=== Commits NgRx Auth & Session ===" -ForegroundColor Green

# Commit 1 - Installation et wiring NgRx
$env:GIT_AUTHOR_DATE = "2026-03-13T16:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T16:30:00"
git add passerelle-rh-frontend/package.json
git add passerelle-rh-frontend/src/app/app.config.ts
git commit -m "feat: integration de NgRx Store et Effects pour la gestion globale de la session auth (APP_INITIALIZER + store auth)"

# Commit 2 - Feature store auth (state, actions, reducer, selectors, effects, facade)
$env:GIT_AUTHOR_DATE = "2026-03-13T17:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T17:00:00"
git add passerelle-rh-frontend/src/app/store/auth/auth.models.ts
git add passerelle-rh-frontend/src/app/store/auth/auth.actions.ts
git add passerelle-rh-frontend/src/app/store/auth/auth.reducer.ts
git add passerelle-rh-frontend/src/app/store/auth/auth.selectors.ts
git add passerelle-rh-frontend/src/app/store/auth/auth.effects.ts
git add passerelle-rh-frontend/src/app/store/auth/auth.facade.ts
git commit -m "feat: ajout du feature store auth (state, actions, reducer, selectors, effects, facade NgRx)"

# Commit 3 - LoginComponent branche sur NgRx (AuthFacade)
$env:GIT_AUTHOR_DATE = "2026-03-13T17:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T17:30:00"
git add passerelle-rh-frontend/src/app/components/login/login.component.ts
git commit -m "refactor: migration de LoginComponent vers AuthFacade NgRx avec gestion du loading et des erreurs"

# Commit 4 - Guards auth/admin connectes au store
$env:GIT_AUTHOR_DATE = "2026-03-13T17:50:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T17:50:00"
git add passerelle-rh-frontend/src/app/guards/auth.guard.ts
git add passerelle-rh-frontend/src/app/guards/admin.guard.ts
git commit -m "feat: mise a jour des guards auth et admin pour utiliser le store NgRx avec fallback AuthService"

# Commit 5 - Interceptor + resolver synchronises avec le store
$env:GIT_AUTHOR_DATE = "2026-03-13T18:10:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T18:10:00"
git add passerelle-rh-frontend/src/app/interceptors/auth.interceptor.ts
git add passerelle-rh-frontend/src/app/resolvers/profile.resolver.ts
git commit -m "feat: synchronisation de l interceptor JWT et du profileResolver avec le store NgRx et le logout global"

# Commit 6 - Tests NgRx (reducer) et stabilisation AppComponent
$env:GIT_AUTHOR_DATE = "2026-03-13T18:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-13T18:30:00"
git add passerelle-rh-frontend/src/app/store/auth/auth.reducer.spec.ts
git add passerelle-rh-frontend/src/app/app.component.spec.ts
git commit -m "test: ajout des tests unitaires du reducer auth NgRx et assouplissement du test AppComponent"

# Nettoyage des variables d'environnement
Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== Derniers commits ===" -ForegroundColor Green
git log --oneline -10

Write-Host "`n=== Push vers origin/master ===" -ForegroundColor Yellow
git push origin master

Write-Host "`n=== Termine ! NgRx Auth/Session + tests pousses ===" -ForegroundColor Green

