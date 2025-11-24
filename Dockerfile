# build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/quarkus-app/ /app/quarkus-app/
EXPOSE 8081
CMD ["java", "-jar", "/app/quarkus-app/quarkus-run.jar"]
