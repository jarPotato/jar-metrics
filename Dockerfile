FROM gradle:7.6-jdk AS build

WORKDIR /usr/app/

COPY src/main ./src/main
COPY build.gradle .

COPY build.gradle settings.gradle  ./

RUN gradle clean build

FROM openjdk:17.0.1-jdk-slim AS run

WORKDIR /usr/app/

RUN adduser --system --group app-user

COPY --from=build --chown=app-user:app-user /usr/app/build/libs/*.jar app.jar

USER app-user

CMD ["java", "-jar", "app.jar"]