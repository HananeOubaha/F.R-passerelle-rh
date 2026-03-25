-- Initialisation des données de démo pour Passerelle RH
-- Mot de passe haché pour '123456' : $2a$10$eAccYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORr/M55AtjHGkHKzX2
-- (Ce hash BCrypt est générique, il faudra vérifier s'il correspond bien à l'algorithme utilisé par votre Spring Security,
-- sinon un nouveau hash devra être généré. Par défaut Spring Security utilise BCrypt avec force 10)

-- 1. Référentiel de Compétences
INSERT INTO competences (nom) VALUES ('Gestion de projet') ON CONFLICT DO NOTHING;
INSERT INTO competences (nom) VALUES ('Communication') ON CONFLICT DO NOTHING;
INSERT INTO competences (nom) VALUES ('Java') ON CONFLICT DO NOTHING;
INSERT INTO competences (nom) VALUES ('Angular') ON CONFLICT DO NOTHING;
INSERT INTO competences (nom) VALUES ('Travail en équipe') ON CONFLICT DO NOTHING;
INSERT INTO competences (nom) VALUES ('Autonomie') ON CONFLICT DO NOTHING;
INSERT INTO competences (nom) VALUES ('Marketing Digital') ON CONFLICT DO NOTHING;

-- 2. Référentiel de Badges
INSERT INTO badges (nom, description, seuil_score, icone_url, message_felicitation, niveau)
VALUES ('Badge Débutant', 'Premier pas dans le monde pro', 10.0, '🥉', 'Bravo pour vos débuts !', 'DEBUTANT') ON CONFLICT DO NOTHING;

INSERT INTO badges (nom, description, seuil_score, icone_url, message_felicitation, niveau)
VALUES ('Badge Confirmé', 'Une expérience solide validée', 50.0, 'mw-badge-silver', 'Vous progressez vite !', 'CONFIRME') ON CONFLICT DO NOTHING;

INSERT INTO badges (nom, description, seuil_score, icone_url, message_felicitation, niveau)
VALUES ('Badge Expert', 'Excellence et fiabilité reconnues', 90.0, '🥇', 'Excellent travail !', 'EXPERT') ON CONFLICT DO NOTHING;

-- 3. Utilisateurs (ADMIN, VALIDATEUR, USER)

-- Admin
INSERT INTO users (dtype, nom, prenom, email, password, role, ville, score_fiabilite, profile_public_token)
VALUES ('Administrateur', 'Admin', 'Super', 'admin@test.com', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORr/M55AtjHGkHKzX2', 'ADMIN', 'Paris', 0.0, 'admin-token-secret') ON CONFLICT DO NOTHING;

-- Validateur (Employeur)
INSERT INTO users (dtype, nom, prenom, email, password, role, ville, score_fiabilite, profile_public_token, entreprise, poste)
VALUES ('Validateur', 'oubaha', 'hicham', 'hicham@gmail.com', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORr/M55AtjHGkHKzX2', 'VALIDATOR', 'casa', 0.0, 'validateur-token', 'Tech Corp', 'CTO') ON CONFLICT DO NOTHING;

-- Utilisateur 1 (Jeune Talent avec profil bien rempli)
INSERT INTO users (dtype, nom, prenom, email, password, role, ville, score_fiabilite, profile_public_token)
VALUES ('Utilisateur', 'Martin', 'Sophie', 'sophie.martin@test.com', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORr/M55AtjHGkHKzX2', 'USER', 'Bordeaux', 85.0, 'a1b2c3d4-sophie') ON CONFLICT DO NOTHING;

-- Utilisateur 2 (Débutant)
INSERT INTO users (dtype, nom, prenom, email, password, role, ville, score_fiabilite, profile_public_token)
VALUES ('Utilisateur', 'Dubois', 'Thomas', 'thomas.dubois@test.com', '$2a$10$eAccYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORr/M55AtjHGkHKzX2', 'USER', 'Toulouse', 20.0, 'e5f6g7h8-thomas') ON CONFLICT DO NOTHING;

-- 4. Attribution de Passeports RH (nécessaire pour lier les badges)
INSERT INTO passeport_rh (utilisateur_id) SELECT id FROM users WHERE email = 'sophie.martin@test.com' ON CONFLICT DO NOTHING;
INSERT INTO passeport_rh (utilisateur_id) SELECT id FROM users WHERE email = 'thomas.dubois@test.com' ON CONFLICT DO NOTHING;

-- 5. Lancer les liens User - Passeport (mise à jour de la FK dans user s'il y a lieu, dépend du mapping, ici on suppose OneToOne mappedBy côté User)
UPDATE users SET passeport_id = (SELECT id FROM passeport_rh WHERE utilisateur_id = users.id) WHERE email IN ('sophie.martin@test.com', 'thomas.dubois@test.com');

-- 6. Attribution de Compétences aux Utilisateurs (Table de jointure user_competences)
INSERT INTO user_competences (user_id, competence_id)
SELECT u.id, c.id FROM users u, competences c WHERE u.email = 'sophie.martin@test.com' AND c.nom IN ('Java', 'Angular', 'Travail en équipe') ON CONFLICT DO NOTHING;

INSERT INTO user_competences (user_id, competence_id)
SELECT u.id, c.id FROM users u, competences c WHERE u.email = 'thomas.dubois@test.com' AND c.nom IN ('Communication') ON CONFLICT DO NOTHING;


-- 7. Missions (Expériences)

-- Mission 1: Validée pour Sophie
INSERT INTO missions (titre, description, date_debut, date_fin, statut, utilisateur_id, validator_email)
VALUES ('Développement Fullstack Stage', 'Développement d''une application web RH en Java/Angular.', '2025-01-10', '2025-06-30', 'VALIDATED', (SELECT id FROM users WHERE email='sophie.martin@test.com'), 'validateur@test.com');

-- Mission 2: En attente pour Sophie
INSERT INTO missions (titre, description, date_debut, date_fin, statut, utilisateur_id, validator_email)
VALUES ('Bénévolat Association', 'Aide à l''organisation d''événements caritatifs.', '2025-07-01', '2025-07-15', 'PENDING', (SELECT id FROM users WHERE email='sophie.martin@test.com'), 'asso@test.com');

-- Mission 3: Validée pour Sophie (Ancienne)
INSERT INTO missions (titre, description, date_debut, date_fin, statut, utilisateur_id, validator_email)
VALUES ('Job Été Vendeuse', 'Vente et conseil client en magasin de sport.', '2024-07-01', '2024-08-31', 'VALIDATED', (SELECT id FROM users WHERE email='sophie.martin@test.com'), 'shop@test.com');

-- Mission 4: Validée pour Thomas
INSERT INTO missions (titre, description, date_debut, date_fin, statut, utilisateur_id, validator_email)
VALUES ('Stage Observation', 'Découverte des métiers de la banque.', '2025-09-01', '2025-09-05', 'VALIDATED', (SELECT id FROM users WHERE email='thomas.dubois@test.com'), 'banque@test.com');


-- 8. Validations (Lien Mission - Validateur + Commentaire)

-- Validation Mission 1 (Sophie)
INSERT INTO validations (date_validation, commentaire, mission_id, validateur_id)
VALUES ('2025-07-05T10:00:00', 'Excellent travail, très autonome et force de proposition.', (SELECT id FROM missions WHERE titre = 'Développement Fullstack Stage'), (SELECT id FROM users WHERE email='validateur@test.com'));

-- Validation Mission 3 (Sophie - Validateur externe non inscrit en base en tant que User, ici on simule qu'il n'est pas lié à un ID validateur interne ou on utilise le validateur générique)
-- *Note: Si votre modèle impose un validateur_id non null, on utilise le compte validateur de test.*
INSERT INTO validations (date_validation, commentaire, mission_id, validateur_id)
VALUES ('2024-09-01T14:30:00', 'Ponctuelle et souriante, très bonne relation client.', (SELECT id FROM missions WHERE titre = 'Job Été Vendeuse'), (SELECT id FROM users WHERE email='validateur@test.com'));

-- Validation Mission 4 (Thomas)
INSERT INTO validations (date_validation, commentaire, mission_id, validateur_id)
VALUES ('2025-09-06T09:00:00', 'Curieux et intéressé.', (SELECT id FROM missions WHERE titre = 'Stage Observation'), (SELECT id FROM users WHERE email='validateur@test.com'));


-- 9. Attribution des Badges aux Passeports (Table de jointure passeport_badges)
-- Sophie a > 50 points (85.0), elle a Débutant et Confirmé
INSERT INTO passeport_badges (passeport_id, badge_id)
SELECT p.id, b.id FROM passeport_rh p, badges b
WHERE p.utilisateur_id = (SELECT id FROM users WHERE email='sophie.martin@test.com') AND b.niveau IN ('DEBUTANT', 'CONFIRME') ON CONFLICT DO NOTHING;

-- Thomas a 20 points, il a Débutant
INSERT INTO passeport_badges (passeport_id, badge_id)
SELECT p.id, b.id FROM passeport_rh p, badges b
WHERE p.utilisateur_id = (SELECT id FROM users WHERE email='thomas.dubois@test.com') AND b.niveau = 'DEBUTANT' ON CONFLICT DO NOTHING;

