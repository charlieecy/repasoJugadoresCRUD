plugins {
    kotlin("jvm") version "2.2.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // BBDD H2
    implementation("com.h2database:h2:2.2.224")

    // JDBI
    // Reflexión
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Core
    implementation("org.jdbi:jdbi3-core:3.48.0")
    // SQL Object
    implementation("org.jdbi:jdbi3-sqlobject:3.48.0")
    // Extensión para Kotlin
    implementation("org.jdbi:jdbi3-kotlin:3.48.0")
    // Extensión de SQL Object para Kotlin
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.48.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}