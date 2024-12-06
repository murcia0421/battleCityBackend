FROM maven:3-openjdk-17 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
VOLUME /tmp

LABEL maintainer="tu-email@example.com"
LABEL version="1.0.0"
LABEL description="Aplicación Java construida con Maven"

COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
