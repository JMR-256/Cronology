FROM --platform=linux/amd64 eclipse-temurin:17-alpine
LABEL authors="jry22"
WORKDIR /usr/src/app

# Copy source files and gradle build
COPY CronParser/ CronParser/
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .

# Build app
RUN ./gradlew :CronParser:build --no-daemon

ENTRYPOINT ["java", "-jar", "CronParser/build/libs/CronParser.jar"]