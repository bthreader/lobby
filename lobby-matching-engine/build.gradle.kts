plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    id("io.freefair.lombok").version("8.4")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Core
    implementation(project(":lobby-core"))

    // Use JUnit Jupiter for testing
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:32.1.1-jre")

    // Protocol
    implementation(project(":lobby-protocol"))

    // Efficient data structures
    implementation(libs.agrona)

    // Logger
    implementation(libs.slf4j)
    implementation(libs.logback)

    // Mockito
    testImplementation(libs.mockito)

    // AssertJ
    testImplementation(libs.assertj)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("lobby.matching.engine.MatchingEngineApplication")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
