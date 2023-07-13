plugins {
    id("com.github.luchici.java-conventions")
    id("org.flywaydb.flyway") version "9.20.0"
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.1.0"
}

dependencies {

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok:")
    testAnnotationProcessor("org.projectlombok:lombok")

    api("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql:42.3.7")
    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:1.18.3")
    testImplementation("org.testcontainers:postgresql")
}

description = "AuthService"


flyway {
    url = "jdbc:postgresql://localhost:5432/SmartCity-Auth"
    user = "postgres"
    password = "Ireland123"
    cleanDisabled = false
}
