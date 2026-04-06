# ==========================================
# Stage 1: Build the Ktor application
# ==========================================
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# On copie tout le code source
COPY . .

# On s'assure que le wrapper a les droits d'exécution (souvent perdu via Git/Windows)
RUN chmod +x ./gradlew

# On compile l'API en utilisant TON Gradle Wrapper local
RUN ./gradlew :server:api:installDist --no-daemon

# ==========================================
# Stage 2: Create the minimal runtime image
# ==========================================
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
WORKDIR /app

# On récupère le build généré par le wrapper
COPY --from=build /app/server/api/build/install/api /app/

WORKDIR /app/bin

# Start the Ktor application
CMD ["./api"]