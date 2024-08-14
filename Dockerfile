# Use a base image with Java 17
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container at /app
COPY target/URL-Shortener-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8081

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]