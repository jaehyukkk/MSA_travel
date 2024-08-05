import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
	id("org.springframework.boot") version "2.7.6"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("kapt") version "1.5.30"
	idea
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
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client:3.1.6")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// 코틀린 lazy 로딩 이슈 관련
	implementation("org.jetbrains.kotlin:kotlin-allopen:1.6.0")

	// database
	implementation("org.mariadb.jdbc:mariadb-java-client:3.0.5")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	//querydsl
	implementation("com.querydsl:querydsl-jpa:5.0.0")
	implementation("com.querydsl:querydsl-apt:5.0.0")
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	implementation("javax.persistence:javax.persistence-api:2.2")
	annotationProcessor(group = "com.querydsl", name = "querydsl-apt", classifier = "jpa")
	kapt("com.querydsl:querydsl-apt:5.0.0:jpa")

	//swagger
	implementation("org.springdoc:springdoc-openapi-ui:1.6.12")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")

	//json
	implementation("com.google.code.gson:gson:2.10.1")

	//test
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
}

// Kotlin QClass Setting
kotlin.sourceSets.main {
	println("kotlin sourceSets buildDir:: $buildDir")
	setBuildDir("$buildDir")
}

idea {
	module {
		val kaptMain = file("build/generated/source/kapt/main")
		sourceDirs.add(kaptMain)
		generatedSourceDirs.add(kaptMain)
	}
}


// 코틀린 lazy 로딩 이슈 관련
allOpen {
	annotation("javax.persistence.Entity")
//	annotation("com.ilogistic.delivery_admin_backend.annotation.AllOpen")
}

// 코틀린 lazy 로딩 이슈 관련
apply(plugin = "kotlin-allopen")

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "17"
}
