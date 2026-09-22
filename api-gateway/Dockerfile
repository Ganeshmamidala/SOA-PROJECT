FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.war app.war

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.war"]