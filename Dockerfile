# Stage 1: Build
FROM gradle:7.6.1-jdk17 AS build
WORKDIR /app

# Копируем wrapper и project files правильно
COPY gradlew gradlew.bat ./
COPY gradle ./gradle/
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY server ./server
COPY shared ./shared

# Build only the server module directly
WORKDIR /app/server
RUN chmod +x ../gradlew \
  && ../gradlew installDist -x test \
  && ls -la build/install/server/

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/server/build/install/server/ ./

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["bin/server"]
