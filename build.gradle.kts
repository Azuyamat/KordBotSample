plugins {
    kotlin("jvm") version "1.9.0"
    application
    /**
     * This plugin is used to create a fat jar.
     * id("io.ktor.plugin") version "2.3.4"
     */
}

group = "com.azuyamat"
// Adjust version below
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // Remove line below if not using Kormmand
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:0.11.1") // Kord
    implementation("io.github.cdimascio:dotenv-java:3.0.0") // DotEnv
    implementation("org.mongodb:mongodb-driver-kotlin-sync:4.10.2") // MongoDB
    implementation("org.slf4j:slf4j-nop:2.0.9") // Logger
    implementation("com.github.Azuyamat:Kormmand:1.3.1") // Kormmand
}

tasks.test {
    useJUnitPlatform()
}

// Uncomment below if using Heroku
// task("stage").dependsOn("buildFatJar")

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

/**
 * Uncomment below if using Ktor
 * ktor {
 *     fatJar {
 *         archiveFileName.set("fat.jar")
 *     }
 * }
 */