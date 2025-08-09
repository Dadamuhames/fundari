import org.apache.tools.ant.filters.ReplaceTokens
import org.springframework.boot.gradle.tasks.run.BootRun


plugins {
    // https://github.com/spring-gradle-plugins/dependency-management-plugin/releases
    id("io.spring.dependency-management") version "1.1.7"

    // https://github.com/spring-projects/spring-boot/releases
    id("org.springframework.boot") version "3.4.7"

    // https://github.com/n0mer/gradle-git-properties/releases
    id("com.gorylenko.gradle-git-properties") version "2.4.1"

    // https://github.com/spotbugs/spotbugs-gradle-plugin/releases
    id("com.github.spotbugs") version "6.0.7"

    // https://github.com/diffplug/spotless/tree/master/plugin-gradle
    // https://mvnrepository.com/artifact/com.diffplug.spotless/spotless-plugin-gradle
//	id("com.diffplug.spotless") version "6.25.0"

    // https://github.com/researchgate/gradle-release
    // https://mvnrepository.com/artifact/net.researchgate.release/net.researchgate.release.gradle.plugin
    id("net.researchgate.release") version "3.0.2"

    // https://github.com/ben-manes/gradle-versions-plugin/releases
    id("com.github.ben-manes.versions") version "0.51.0"

    java
    idea
    kotlin("jvm")
}

group = "com.msd"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    all {
        exclude("org.springframework.boot", "spring-boot-starter-logging")

        // Can"t exclude because of this: https://github.com/testcontainers/testcontainers-java/issues/970
        // exclude("junit", "junit")
    }
}

configurations.named("spotbugs").configure {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.ow2.asm") {
            useVersion("9.5")
            because("Asm 9.5 is required for JDK 21 support")
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
    maven {
        name = "Central Portal Snapshots"
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
}


dependencyManagement {
    imports {
        // https://github.com/spring-projects/spring-boot/releases
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.3.5")

        // To avoid specifying the version of each dependency, use a BOM or Bill Of Materials.
        // https://github.com/testcontainers/testcontainers-java/releases
        mavenBom("org.testcontainers:testcontainers-bom:1.18.3")

        //https://immutables.github.io/
        mavenBom("org.immutables:bom:2.9.2")

        mavenBom("org.springframework.ai:spring-ai-bom:1.0.0")
    }

    dependencies {
        // https://github.com/apache/logging-log4j2/tags
        dependencySet("org.apache.logging.log4j:2.20.0") {
            entry("log4j-core")
            entry("log4j-api")
            entry("log4j-web")
        }
    }
}

dependencies {
    // SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.3")
    testCompileOnly("com.github.spotbugs:spotbugs-annotations:4.8.3")
    spotbugsPlugins("jp.skypencil.findbugs.slf4j:bug-pattern:1.5.0@jar")
    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.13.0")

    // Immutables
    annotationProcessor("org.immutables:value")
    compileOnly("org.immutables:builder")
    compileOnly("org.immutables:value-annotations")

    // Spring Boot
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.liquibase:liquibase-core")

    // Spring AI
    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")

    // google


    implementation("com.google.api-client:google-api-client:2.6.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.35.0")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20250616-2.0.0")



    // Other dependencies
    implementation("me.paulschwarz:spring-dotenv:4.0.0")
    implementation("commons-codec:commons-codec:1.17.1")
    implementation("org.mapstruct:mapstruct:1.6.2")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.2")

    implementation("org.telegram:telegrambots-spring-boot-starter:6.9.7.1")

    implementation("org.telegram:telegrambots-client:7.2.0")
    implementation("org.telegram:telegrambots-extensions:7.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("org.reflections:reflections:0.10.2")


    // Logging
    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("org.slf4j:slf4j-log4j12:2.0.16")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(kotlin("stdlib-jdk8"))
}

spotbugs {
    toolVersion.set("4.7.3")
    excludeFilter.set(file("${project.rootDir}/findbugs-exclude.xml"))
}



tasks {
    spotbugsMain {
        effort.set(com.github.spotbugs.snom.Effort.MAX)
        reports.create("html") {
            enabled = true
        }
    }

    val bootRun by getting(BootRun::class) {
        jvmArgs = listOf("-Duser.timezone=Asia/Tashkent")
    }

    spotbugsTest {
        ignoreFailures = true
        reportLevel.set(com.github.spotbugs.snom.Confidence.HIGH)
        effort.set(com.github.spotbugs.snom.Effort.MIN)
        reports.create("html") {
            enabled = true
        }
    }
}

tasks.compileJava {
    dependsOn("processResources")
    options.release.set(21)
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:deprecation"))
}

tasks.processResources {
    val tokens = mapOf(
        "application.version" to project.version,
        "application.description" to project.description
    )
    filesMatching("**/*.yml") {
        filter<ReplaceTokens>("tokens" to tokens)
    }
}

tasks.test {
    failFast = false
    enableAssertions = true

    // Enable JUnit 5 (Gradle 4.6+).
    useJUnitPlatform()

    testLogging {
        events("PASSED", "STARTED", "FAILED", "SKIPPED")
        // Set to true if you want to see output from tests
        showStandardStreams = false
        setExceptionFormat("FULL")
    }

    systemProperty("io.netty.leakDetectionLevel", "paranoid")
}

defaultTasks("spotlessApply", "build")


kotlin {
    jvmToolchain(21)
}