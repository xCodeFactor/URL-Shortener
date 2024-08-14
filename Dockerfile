# Use Maven 3.9.8 to build the JAR file with OpenJDK 17
FROM maven:3.9.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK image to run the application
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/URL-Shortener-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
