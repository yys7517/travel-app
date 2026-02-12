# 1단계: 빌드 (Gradle로 JAR 파일 생성)
FROM gradle:8.5-jdk17 AS build

# EC2 배포용
# FROM --platform=linux/amd64 gradle:8.5-jdk17 AS build

WORKDIR /app

# Gradle 파일 먼저 복사 (캐싱 최적화)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# 의존성 다운로드 (이 레이어는 캐싱됨)
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# 빌드 (테스트 제외)
RUN gradle build -x test --no-daemon

# 2단계: 실행 (경량화된 JRE 이미지)
FROM bellsoft/liberica-openjdk-alpine:17

# FROM eclipse-temurin:17-jre-alpine

# EC2 배포용
# FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]