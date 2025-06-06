plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:32.1.1-jre")

    // https://mvnrepository.com/artifact/uk.co.real-logic/sbe-tool
    implementation("uk.co.real-logic:sbe-tool:1.30.0")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

sourceSets.main {
    java.srcDirs("generated/src/main/java")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

tasks.register<JavaExec>("generateMessages") {
    mainClass = "uk.co.real_logic.sbe.SbeTool"
    classpath = sourceSets.main.get().runtimeClasspath

    systemProperties(
            mapOf(
                    "sbe.output.dir" to "generated/src/main/java",
                    "sbe.target.language" to "Java",
                    "sbe.validation.stop.on.error" to "true",
                    "sbe.validation.xsd" to "src/main/resources/sbe/sbe.xsd",
            )
    )

    args = listOf("src/main/resources/messages.xml")
}
