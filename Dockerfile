FROM maven:3.9.5 as builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests


FROM eclipse-temurin:17-jdk-jammy

COPY --from=builder /app/target/marketplace-0.0.1-SNAPSHOT.war /app.war

EXPOSE 8443

CMD ["java", "-jar", "/app.war"]


