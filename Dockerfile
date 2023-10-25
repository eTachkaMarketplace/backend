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

FROM maven:3.9.5 as builder

WORKDIR /app

COPY pom.xml .

RUN mvn install -DskipTests

COPY src src
RUN mv target/marketplace-0.0.1-SNAPSHOT.war /marketplace.war
RUN #mvn package -DskipTests  \
RUN mvn clean install -Dmaven.test.skip=true

RUN #mv marketplace-0.0.1-SNAPSHOT.war /marketplace.war


FROM openjdk:17-jdk-alpine

COPY --from=builder /marketplace.war /app.war

WORKDIR /marketplace

CMD ["java", "-jar", "/marketplace.war"]
