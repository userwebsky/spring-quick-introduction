# Base image for build
FROM openjdk:11 as draft
# Working directory
WORKDIR /opt/app
# Copy maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ["chmod", "+x", "./mvnw"]
# Maven build with dependency
RUN ./mvnw dependency:go-offline
# Copy app file
COPY ./src ./src
RUN ./mvnw clean install

# Base image for app
FROM eclipse-temurin:11-jre-alpine
# I copy the file
COPY --from=draft /opt/app/target/*.jar /opt/app/library.jar
# Set the communication port
EXPOSE 2023
# Execute the start command
CMD ["java", "-jar", "./opt/app/library.jar"]