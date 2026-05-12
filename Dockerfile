FROM gradle:9.4.1-jdk21 AS build
WORKDIR /app

COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN gradle dependencies --no-daemon

COPY src src

RUN gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]