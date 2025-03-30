FROM gradle:8.4-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle :bank-service:bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /home/gradle/project/bank-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

