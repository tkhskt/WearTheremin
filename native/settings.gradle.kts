pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url =  "https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven(url =  "https://jitpack.io")
    }
}
rootProject.name = "Theremin"
include(":mobile")
include(":wear")
include(":redux")
include(":feature-theremin")
include(":core-ui")
include(":feature-tutorial")
include(":feature-license")
include(":core-data")
include(":domain")
include(":infra")
