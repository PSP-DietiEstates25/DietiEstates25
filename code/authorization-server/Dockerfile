# syntax=docker/dockerfile:1

FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copio solo ciò che serve per scaricare le dipendenze (cache layer)
COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle
RUN chmod +x /app/gradlew

# Copio i sorgenti
COPY src /app/src

# Build (senza test per velocizzare in CI)
RUN /app/gradlew --no-daemon bootJar -x test

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

ENV JAVA_OPTS=""
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]