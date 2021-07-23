tasks.named("bootJar") {
    enabled = false
}

tasks.named("jar") {
    enabled = true
}

dependencies {
    api("com.querydsl:querydsl-core")
    api("com.querydsl:querydsl-jpa")
    annotationProcessor("com.querydsl:querydsl-apt:${dependencyManagement.importedProperties["querydsl.version"]}:jpa")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly ("com.h2database:h2")
}

val generated="src/main/generated"
sourceSets {
    main {
        java.srcDirs(generated)
    }
}

tasks.compileJava {
    options.generatedSourceOutputDirectory.set(file(generated))
}

tasks.clean {
    file(generated).delete()
}