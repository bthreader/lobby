plugins {
    `java-library`
    id("io.freefair.lombok").version("8.4")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing
    testImplementation(libs.junit)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:32.1.1-jre")

    // Efficient data structures
    implementation(libs.agrona)

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

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
