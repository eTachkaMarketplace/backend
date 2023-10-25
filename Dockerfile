FROM maven:3.9.5 as builder

WORKDIR /app

COPY pom.xml .

RUN mvn clean install -DskipTests

COPY src src

RUN mv target/marketplace-0.0.1-SNAPSHOT.war /marketplace.war

FROM openjdk:17-jdk-alpine

COPY --from=builder /marketplace.war /marketplace.war

WORKDIR /marketplace

EXPOSE 8443

CMD ["java", "-jar", "/marketplace.war"]