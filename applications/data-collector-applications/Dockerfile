# Use Amazon Corretto 17 based on Alpine Linux for a lightweight image
FROM amazoncorretto:17-alpine3.19

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
# Update the JAR_FILE path if your build output directory or JAR naming convention is different
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file 
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
