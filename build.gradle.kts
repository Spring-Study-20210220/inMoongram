plugins {
    java
    `java-library`
}

buildscript {
    extra["springBootVersion"] = "2.5.2"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${rootProject.extra["springBootVersion"]}")
        classpath("io.spring.gradle:dependency-management-plugin:1.0.11.RELEASE")
    }
}

allprojects {
    group = "com.pair"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencies {
        api("org.springframework.boot:spring-boot-starter-security")
        api("org.springframework.security:spring-security-oauth2-client")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("com.google.guava:guava:30.1.1-jre")
        implementation("org.springframework.boot:spring-boot-starter")
        testImplementation ("org.springframework.security:spring-security-test")
    }
}

project(":api") {
    dependencies {
        api(project(":service"))
    }
}

project(":service") {
    dependencies {
        api(project(":repository"))
    }
}