
FROM bellsoft/liberica-openjdk-alpine-musl:17
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "app.jar"]
