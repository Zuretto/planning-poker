FROM gradle:8.0.2-jdk19-alpine AS build

WORKDIR /app

COPY --chown=gradle:gradle . .
RUN gradle build

FROM openjdk:19-jdk-slim
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
