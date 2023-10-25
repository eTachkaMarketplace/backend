#FROM maven:3.9.5 as builder
#
#WORKDIR /app
#
#COPY pom.xml .
#
#RUN mvn clean install -DskipTests
#
#COPY src src
#
#COPY target/marketplace-0.0.1-SNAPSHOT.war /app/marketplace.war
#
#
#FROM eclipse-temurin:17-jdk-jammy
#
#WORKDIR /app
#
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:resolve
#
#EXPOSE 8443
#
#COPY src ./src
#
#CMD ["./mvnw", "spring-boot:run"]

FROM maven:3.9.5 as builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests


FROM eclipse-temurin:17-jdk-jammy

COPY --from=builder /app/target/marketplace-0.0.1-SNAPSHOT.war /app.war

EXPOSE 8443

CMD ["java", "-jar", "/app.war"]


