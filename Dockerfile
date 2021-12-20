# Use maven to compile the java application.
FROM maven:3-jdk-11-slim AS build-env
WORKDIR /app
COPY pom.xml ./
RUN mvn verify --fail-never
COPY . ./
RUN mvn -Dmaven.test.skip=true package

# Build runtime image.
FROM openjdk:11-jre-slim
COPY --from=build-env /app/target/ /app/
CMD ["java", "-jar", "/app/chat-0.0.1.jar"]
