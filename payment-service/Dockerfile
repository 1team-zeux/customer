FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.6.0/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
COPY --from=builder /app/build/libs/app.jar app.jar
EXPOSE 17002
ENTRYPOINT ["java", "-jar", "app.jar"]
