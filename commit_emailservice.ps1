# Commit EmailService - Passerelle RH
cd C:\Users\safiy\Desktop\passerelle-rh

$env:GIT_AUTHOR_DATE = "2026-03-12T14:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T14:00:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/service/EmailService.java
git commit -m "feat: creation du EmailService avec envoi d emails HTML pour invitation, validation et rejet de mission"

$env:GIT_AUTHOR_DATE = "2026-03-12T14:30:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T14:30:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/PasserelleRhApplication.java
git commit -m "config: activation de @EnableAsync pour l envoi d emails asynchrone"

$env:GIT_AUTHOR_DATE = "2026-03-12T14:45:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T14:45:00"
git add passerelle-rh-backend/src/main/resources/application.yml
git commit -m "config: ajout de app.frontend-url pour les liens dans les emails"

$env:GIT_AUTHOR_DATE = "2026-03-12T15:00:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T15:00:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/controller/MissionController.java
git commit -m "feat: integration EmailService dans MissionController pour notifier le validateur a la creation"

$env:GIT_AUTHOR_DATE = "2026-03-12T15:20:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T15:20:00"
git add passerelle-rh-backend/src/main/java/com/passerellerh/service/ValidationService.java
git add passerelle-rh-backend/src/main/java/com/passerellerh/controller/ValidationController.java
git commit -m "feat: notification email a l utilisateur apres validation ou rejet de sa mission"

$env:GIT_AUTHOR_DATE = "2026-03-12T15:40:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T15:40:00"
git add passerelle-rh-backend/src/test/java/com/passerellerh/service/EmailServiceTest.java
git commit -m "test: ajout de 5 tests unitaires pour EmailService (envoi invitation, notification, erreur graceful)"

$env:GIT_AUTHOR_DATE = "2026-03-12T15:50:00"
$env:GIT_COMMITTER_DATE = "2026-03-12T15:50:00"
git add passerelle-rh-backend/src/test/java/com/passerellerh/service/ValidationServiceTest.java
git add passerelle-rh-backend/src/test/java/com/passerellerh/PasserelleRhApplicationTests.java
git commit -m "fix: correction des tests existants pour integrer le mock EmailService"

Remove-Item Env:\GIT_AUTHOR_DATE
Remove-Item Env:\GIT_COMMITTER_DATE

Write-Host "`n=== 7 commits EmailService crees ===" -ForegroundColor Green
git log --oneline -7
Write-Host "`n=== Push ===" -ForegroundColor Cyan
git push origin master

