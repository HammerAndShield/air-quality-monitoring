plugins {
    kotlin("jvm")
}

group = "org.hammernshield"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Exposed core library
    implementation("org.jetbrains.exposed:exposed-core:0.36.2")
    // Exposed DAO
    implementation("org.jetbrains.exposed:exposed-dao:0.36.2")
    // Exposed JDBC Driver
    implementation("org.jetbrains.exposed:exposed-jdbc:0.36.2")
    // Coroutine support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    // Database driver
    implementation("org.postgresql:postgresql:42.5.1")
    // HikariCP for connection pooling
    implementation("com.zaxxer:HikariCP:5.0.0")
    // For env
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    // For datetime and exposed
    implementation("org.jetbrains.exposed:exposed-java-time:0.36.2")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}