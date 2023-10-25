FROM openjdk:17-jdk-alpine

#ENV LANG=C.UTF-8

WORKDIR /app

COPY target/marketplace-0.0.1-SNAPSHOT.war app.war
COPY src src

EXPOSE 8443

CMD ["java", "-jar", "app.war"]
