
FROM openjdk:17-jdk-slim

# Вказуємо робочу директорію всередині контейнера
WORKDIR /app

# Копіюємо JAR файл в контейнер
COPY target/reservation-system-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080


ENTRYPOINT ["java", "-jar", "app.jar"]