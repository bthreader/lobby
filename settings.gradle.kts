rootProject.name = "lobby"
include("lobby-matching-engine", "lobby-protocol", "lobby-gateway")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("agrona", "org.agrona:agrona:1.20.0")
            library("slf4j", "org.slf4j:slf4j-api:2.0.10")
            library("junit", "org.junit.jupiter:junit-jupiter:5.9.3")
            library("mockito", "org.mockito:mockito-junit-jupiter:5.8.0")
            library("assertj", "org.assertj:assertj-core:3.6.1")
            library("logback", "ch.qos.logback:logback-classic:1.4.14")
        }
    }
}
