FROM amazoncorretto:17-alpine3.19

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file 
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
