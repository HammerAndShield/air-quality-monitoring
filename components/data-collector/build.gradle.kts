
plugins {
    id("java-library")
    kotlin("jvm") version "1.9.22"
}

group = "org.hammernshield"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Core Ktor Client
    api("io.ktor:ktor-client-core:2.3.8")
    //CIO Engine
    api("io.ktor:ktor-client-cio:2.3.8")
    // Ktor Jackson feature
    implementation("io.ktor:ktor-serialization-jackson:2.3.8")
    //For negotiating JSON
    implementation("io.ktor:ktor-client-content-negotiation:2.3.8")

    testImplementation("io.ktor:ktor-client-mock:2.3.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}