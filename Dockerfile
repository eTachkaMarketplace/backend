FROM maven:3.9.3-eclipse-temurin-20

WORKDIR /app

COPY pom.xml .

RUN mvn install -DskipTests

COPY src src
RUN mv target/marketplace-0.0.1-SNAPSHOT.war /marketplace.war
RUN mvn clean install -Dmaven.test.skip=true



FROM eclipse-temurin:17-alpine

COPY --from=builder /marketplace.war /app.war

WORKDIR /marketplace

CMD ["java", "-jar", "/app.war"]
