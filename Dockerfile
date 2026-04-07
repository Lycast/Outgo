# ==========================================
# Stage 1: Build
# ==========================================
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew :server:api:installDist --no-daemon

# ==========================================
# Stage 2: Runtime
# ==========================================
FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
WORKDIR /app
COPY --from=build /app/server/api/build/install/api/ /app/
RUN chmod +x /app/bin/api
CMD ["/app/bin/api"]