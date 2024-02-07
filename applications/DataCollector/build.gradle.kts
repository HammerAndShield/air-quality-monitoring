import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.22"
	kotlin("plugin.spring") version "1.9.22"
}

group = "org.hammernshield"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Exposed core library
	implementation("org.jetbrains.exposed:exposed-core:0.36.2")
	// Exposed DAO
	implementation("org.jetbrains.exposed:exposed-dao:0.36.2")
	// Exposed JDBC Driver
	implementation("org.jetbrains.exposed:exposed-jdbc:0.36.2")
	// Coroutine support
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
	// Database driver (example with PostgreSQL)
	implementation("org.postgresql:postgresql:42.5.1")
	// HikariCP for connection pooling
	implementation("com.zaxxer:HikariCP:5.0.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
