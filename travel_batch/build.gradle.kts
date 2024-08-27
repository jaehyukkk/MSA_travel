import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("plugin.jpa") version "1.8.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

    //elasticsearch
    implementation("org.springframework.data:spring-data-elasticsearch:4.2.12")

    //batch
    implementation("org.springframework.batch:spring-batch-core:4.2.8.RELEASE")
    testImplementation("org.springframework.batch:spring-batch-test:4.3.5")

    // database
    runtimeOnly("com.h2database:h2")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

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
