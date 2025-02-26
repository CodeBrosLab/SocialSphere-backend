FROM openjdk:17-jdk-slim

# Copy the JAR file into the container
COPY target/socialsphere-0.0.1-SNAPSHOT.jar /app.jar

# Expose the port your Spring Boot app is listening on (usually 8080)
EXPOSE 8080

# Specify the command to run when the container starts
CMD ["java", "-jar", "/app.jar"]