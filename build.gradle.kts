plugins {
    java
    id("io.freefair.lombok") version "8.4"
    id("org.springframework.boot") version "3.2.0"
    id("io.sentry.jvm.gradle") version "4.1.1"
}

group = "com.sellbycar.marketplace"
version = "1.0.0"

springBoot {
    mainClass.set("com.sellbycar.marketplace.MarketplaceApplication")
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot", "spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot", "spring-boot-starter-data-jpa")
    implementation("org.springframework.boot", "spring-boot-starter-security")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-mail")
    implementation("org.springframework.boot", "spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot", "spring-boot-starter-tomcat")
    implementation("org.springframework", "spring-context-support")
    implementation("org.springdoc", "springdoc-openapi-starter-webmvc-ui", "2.2.0")
    // Database
    implementation("org.postgresql", "postgresql", "42.7.1")
    implementation("org.flywaydb", "flyway-core", "10.4.1")
    implementation("org.flywaydb", "flyway-database-postgresql", "10.4.1")
    // Validation
    implementation("jakarta.validation", "jakarta.validation-api", "3.1.0-M1")
    // Authentication
    val jjwt = "0.12.3"
    implementation("io.jsonwebtoken", "jjwt-api", jjwt)
    implementation("io.jsonwebtoken", "jjwt-impl", jjwt)
    implementation("io.jsonwebtoken", "jjwt-jackson", jjwt)
    // Mappings
    val mapstruct = "1.5.0.Final"
    implementation("org.mapstruct", "mapstruct", mapstruct)
    annotationProcessor("org.mapstruct", "mapstruct-processor", mapstruct)
    // Utilities
    implementation("me.paulschwarz", "spring-dotenv", "4.0.0")
    implementation("org.jetbrains", "annotations", "24.1.0")
    // Analytics
    implementation("io.sentry", "sentry-spring-boot-starter", "7.1.0")
    // Testing
    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.10.1")
    testImplementation("org.mockito", "mockito-core", "5.8.0")
}

tasks {
    compileJava {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    bootJar {
        archiveFileName.set("server.jar")
    }

    create("stage") {
        dependsOn("bootJar")
    }
}
