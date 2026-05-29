FROM gradle:8.13-jdk21 AS build

WORKDIR /workspace

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle --no-daemon clean bootJar -x test
RUN JAR_FILE="$(ls build/libs/*-SNAPSHOT.jar | grep -v plain | head -n 1)" && cp "$JAR_FILE" /tmp/app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /tmp/app.jar /app/app.jar

EXPOSE 9040

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
