FROM amazoncorretto:17-alpine3.19

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file into the container at /app
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Make port 5000 available to the world outside this container
EXPOSE 5000

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]