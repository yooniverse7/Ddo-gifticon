FROM gradle:8.4-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle :pos-service:bootJar --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /home/gradle/project/pos-service/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
