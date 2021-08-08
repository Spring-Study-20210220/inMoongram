tasks.named("bootJar") {
    enabled = false
}

tasks.named("jar") {
    enabled = true
}

dependencies {
    api ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-mail")
    api ("org.springframework.boot:spring-boot-starter-web")
}