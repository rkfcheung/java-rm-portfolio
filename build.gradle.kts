plugins {
	java
	jacoco
	id("org.springframework.boot") version "2.7.18"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.rkfcheung"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
//	implementation("org.liquibase:liquibase-core")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.check {
	finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.9".toBigDecimal()
			}
		}

		rule {
			isEnabled = false
			element = "CLASS"
			includes = listOf("org.gradle.*")

			limit {
				counter = "LINE"
				value = "TOTALCOUNT"
				maximum = "0.9".toBigDecimal()
			}
		}
	}
}

tasks.testClasses {
	finalizedBy(tasks.jacocoTestReport)
}
