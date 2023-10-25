FROM maven:3.9.5 as builder

WORKDIR /app

COPY pom.xml .

RUN mvn install -DskipTests

COPY src src

RUN mv target/marketplace-0.0.1-SNAPSHOT.war /marketplace.war

RUN mvn clean install -Dmaven.test.skip=true

FROM eclipse-temurin:17-jdk-alpine

COPY --from=builder /marketplace.war /app.war

WORKDIR /marketplace

CMD ["java", "-jar", "/app.war"]
