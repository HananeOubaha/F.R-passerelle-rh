$files = @(
    @{ Path="passerelle-rh-backend/src/main/resources/application.yml"; Msg="config: ajout valeurs par défaut email properties" },
    @{ Path="passerelle-rh-frontend/package-lock.json"; Msg="chore: mise à jour des dépendances frontend" },
    @{ Path="passerelle-rh-frontend/src/app/app.routes.ts"; Msg="feat(routes): mise à jour de la configuration des routes" },
    @{ Path="passerelle-rh-frontend/src/app/components/admin-dashboard/admin-dashboard.component.html"; Msg="style(admin): application du thème bleu sur le dashboard" },
    @{ Path="passerelle-rh-frontend/src/app/components/admin-dashboard/admin-dashboard.component.ts"; Msg="refactor(admin): adaptation logique dashboard" },
    @{ Path="passerelle-rh-frontend/src/app/components/admin-referentials/admin-referentials.component.html"; Msg="style(admin): mise en page page référentiels" },
    @{ Path="passerelle-rh-frontend/src/app/components/admin-users/admin-users.component.html"; Msg="style(admin): mise en page gestion utilisateurs" },
    @{ Path="passerelle-rh-frontend/src/app/components/admin-users/admin-users.component.ts"; Msg="refactor(admin): logique gestion utilisateurs" },
    @{ Path="passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.html"; Msg="style(dashboard): redesign complet avec thème bleu et KPIs" },
    @{ Path="passerelle-rh-frontend/src/app/components/dashboard/dashboard.component.ts"; Msg="refactor(dashboard): gestion du chargement profil et données" },
    @{ Path="passerelle-rh-frontend/src/app/components/login/login.component.html"; Msg="style(auth): redesign page de connexion" },
    @{ Path="passerelle-rh-frontend/src/app/components/missions/missions.component.html"; Msg="style(missions): mise en page liste des missions" },
    @{ Path="passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.html"; Msg="style(passeport): redesign passeport RH et timeline" },
    @{ Path="passerelle-rh-frontend/src/app/components/passeport-rh/passeport-rh.component.ts"; Msg="refactor(passeport): adaptation composant passeport" },
    @{ Path="passerelle-rh-frontend/src/app/components/public-profile/public-profile.component.html"; Msg="style(profile): redesign profil public" },
    @{ Path="passerelle-rh-frontend/src/app/components/register/register.component.html"; Msg="style(auth): redesign page d'inscription" },
    @{ Path="passerelle-rh-frontend/src/app/components/validateur-dashboard/validateur-dashboard.component.html"; Msg="style(validator): redesign dashboard validateur" },
    @{ Path="passerelle-rh-frontend/src/app/components/validateur-dashboard/validateur-dashboard.component.ts"; Msg="refactor(validator): logique dashboard validateur" },
    @{ Path="passerelle-rh-frontend/src/app/components/verify-attestation/verify-attestation.component.html"; Msg="style(verify): redesign page vérification attestation" },
    @{ Path="passerelle-rh-frontend/src/app/services/auth.service.ts"; Msg="feat(auth): restriction accès validation pour admin/validator" },
    @{ Path="passerelle-rh-frontend/src/index.html"; Msg="style: formatage et structure de base" },
    @{ Path="passerelle-rh-frontend/src/styles.css"; Msg="style: définition du thème global et animations" },
    @{ Path="commit_pipes.ps1"; Msg="docs: mise à jour du script de commit pipes" },
    @{ Path="GUIDE_DEMARRAGE.md"; Msg="docs: ajout du guide de démarrage" },
    @{ Path="backdate_commits_final.ps1"; Msg="tools: script pour backdater les commits" },
    @{ Path="commit_ci.ps1"; Msg="tools: script pour commiter la CI" },
    @{ Path="commit_emailservice.ps1"; Msg="tools: script pour commiter le service email" },
    @{ Path="commit_ngrx_auth.ps1"; Msg="tools: script pour commiter ngrx auth" },
    @{ Path="commit_ngrx_missions.ps1"; Msg="tools: script pour commiter ngrx missions" },
    @{ Path="commit_pipes_pagination.ps1"; Msg="tools: script pour commiter les pipes de pagination" },
    @{ Path="commit_reactive_forms.ps1"; Msg="tools: script pour commiter reactive forms" },
    @{ Path="commit_recent_changes.ps1"; Msg="tools: script pour commiter les changements récents" },
    @{ Path="commit_refresh_token.ps1"; Msg="tools: script pour commiter refresh token" },
    @{ Path="commit_resolvers.ps1"; Msg="tools: script pour commiter les resolvers" },
    @{ Path="commit_tests.ps1"; Msg="tools: script pour commiter les tests" },
    @{ Path="passerelle-rh-frontend/list-emojis.js"; Msg="tools: script utilitaire liste emojis" },
    @{ Path="passerelle-rh-frontend/remove-emojis.js"; Msg="tools: script utilitaire suppression emojis" },
    @{ Path="passerelle-rh-frontend/retheme.js"; Msg="tools: script utilitaire retheme" }
)

Write-Host "=== Démarrage des commits individuels ===" -ForegroundColor Cyan

foreach ($file in $files) {
    if (Test-Path $file.Path) {
        Write-Host "Traitement de $($file.Path)..." -ForegroundColor Yellow
        git add $file.Path
        git commit -m $file.Msg
    } else {
        Write-Host "⚠️ Fichier introuvable ou déjà commité : $($file.Path)" -ForegroundColor DarkGray
    }
}

# Gestion du dossier pages (nouveau dossier)
if (Test-Path "passerelle-rh-frontend/src/app/pages") {
    Write-Host "Traitement du dossier pages..." -ForegroundColor Yellow
    git add passerelle-rh-frontend/src/app/pages/
    git commit -m "feat(pages): ajout de la structure des pages"
}

Write-Host "`n=== Récapitulatif ===" -ForegroundColor Green
git log --oneline -n 10

Write-Host "`n=== Push vers origin/master ===" -ForegroundColor Cyan
git push origin master

Write-Host "`n✅ Opération terminée avec succès !" -ForegroundColor Green

