FROM bellsoft/liberica-openjdk-alpine-musl:17
WORKDIR /app

COPY build/libs/*.jar app.jar

COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
