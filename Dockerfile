FROM maven:3.9.5 as builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests


FROM amazoncorretto:17

COPY --from=builder /app/target/marketplace-0.0.1.jar /app.jar

EXPOSE 8443

CMD ["java", "-jar", "/app.jar"]


