FROM dperezcabrera/openjdk11-alpine
ENV TZ America/Sao_Paulo

COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]