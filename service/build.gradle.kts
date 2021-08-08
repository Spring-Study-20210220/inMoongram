tasks.named("bootJar") {
    enabled = false
}

tasks.named("jar") {
    enabled = true
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation ("org.springframework.boot:spring-boot-starter-mail")
    api ("org.springframework.boot:spring-boot-starter-web")
}