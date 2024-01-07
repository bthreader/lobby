rootProject.name = "lobby"
include("lobby-matching-engine", "lobby-protocol", "lobby-gateway")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("agrona", "org.agrona:agrona:1.20.0")
            // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
            library("slf4j", "org.slf4j:slf4j-api:2.0.10")
            library("junit", "org.junit.jupiter:junit-jupiter:5.9.3")
            library("mockito", "org.mockito:mockito-junit-jupiter:5.8.0")
        }
    }
}
