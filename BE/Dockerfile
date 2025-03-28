# 1단계: 빌드용 이미지
FROM gradle:8.4-jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle clean build -x test

# 2단계: 실행용 이미지
FROM openjdk:17-jdk-slim

WORKDIR /app

# 빌드 결과 JAR 복사 (버전에 맞게 수정)
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
