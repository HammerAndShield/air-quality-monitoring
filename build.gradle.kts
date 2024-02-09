import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.hammernshield"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}