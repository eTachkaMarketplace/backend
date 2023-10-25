#FROM openjdk:17-jdk-alpine
#
##ENV LANG=C.UTF-8
#
#WORKDIR /app
#
#COPY target/marketplace-0.0.1-SNAPSHOT.war app.war
#COPY src src
#
#EXPOSE 8443
#
#CMD ["java", "-jar", "app.war"]

FROM maven:3.8.4-openjdk-11-slim as builder

WORKDIR /app

COPY pom.xml .

RUN mvn install -DskipTests

COPY src src

RUN mvn package -DskipTests

RUN mv app.war /app.war

FROM openjdk:11-jre-slim

COPY --from=builder /app.war /app.war

WORKDIR /marketplace

CMD ["java", "-jar", "/app.war"]
