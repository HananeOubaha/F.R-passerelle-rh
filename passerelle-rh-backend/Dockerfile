# ---- Stage 1: Build ----
# Utiliser une image Maven officielle qui inclut déjà les outils nécessaires
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copier uniquement le pom.xml pour télécharger les dépendances
COPY pom.xml .
# Télécharger les dépendances. Cette couche sera mise en cache par Docker.
RUN mvn dependency:go-offline

# Copier le reste du code source
COPY src ./src
# Compiler l'application et créer le package
RUN mvn package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copier le JAR final depuis l'étape de build
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
