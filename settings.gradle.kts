rootProject.name = "lobby"
include("lobby-matching-engine", "lobby-message-codecs")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("agrona", "org.agrona:agrona:1.20.0")
        }
    }
}
