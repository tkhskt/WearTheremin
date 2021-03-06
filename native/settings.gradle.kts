pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
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
