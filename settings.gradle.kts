pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                maven { url  = uri ("https://your.custom.repository.url/") }
                maven { url = uri("https://your-repo-url") }
            }
        }
        maven { url = uri("https://jitpack.io") }
        mavenLocal()
        // Or if it's in a private repository:
        maven { url = uri("https://your-private-repo-url") }
        mavenCentral()
        gradlePluginPortal()


    }

}



dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "SearchAI"
include(":app")
include(":onboarding")
include(":api")
include(":auth")
include(":common")
include(":profile")
include(":myroom")
include(":camera")
include(":explore")
include(":messaging")
include(":webrtc")