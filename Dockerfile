# Use OpenJDK 21 base image
FROM openjdk:21-slim

# Set the working directory inside the container (Path is dynamic will be created inside docker file system)
WORKDIR /app/auth-service

# Copy your WAR file into the container (adjust paths as necessary)
COPY auth-web/build/libs/auth-web.war /app/auth-service/auth-web.war
COPY resources/application.properties /app/auth-service/resources/application.properties

# Expose the necessary port for the Spring Boot application (default is 8080, but you're debugging on port 7000)
EXPOSE 8181
EXPOSE 7001

# Set JAVA_HOME (not necessary if you're using OpenJDK base image, but added for completeness)
# ENV JAVA_HOME="/home/asite/Desktop/Bhavik/Sboot_project/jdk-21.0.1"
# ENV PATH="${JAVA_HOME}/bin:${PATH}"
# ENV JAVA_HOME="/usr/local/openjdk-21"
# ENV PATH="$JAVA_HOME/bin:$PATH"


# Command to run the WAR file with custom java options (debugging and Spring properties)
CMD ["java", "-Xdebug", "-Xrunjdwp:server=y,transport=dt_socket,address=7001,suspend=n", \
    "-Djava.locale.providers=COMPAT,CLDR", \
    "-Djboss.server.home.dir=/app/auth-service", \
    "-jar", "auth-web.war", \
    "--spring.config.location=resources/application.properties"]
