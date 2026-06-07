FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar
EXPOSE 17002
ENTRYPOINT ["java", \
  "-XX:MaxMetaspaceSize=256m", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
