# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/inventory-management-system-1.0-SNAPSHOT.jar app.jar

# Make port 8090 available to the world outside this container
EXPOSE 8090

# Run the app.jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
