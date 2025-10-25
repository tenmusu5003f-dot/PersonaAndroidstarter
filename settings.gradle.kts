// build.gradle.kts (project root)

rootProject.name = "PersonaAndroidstarter"
include(":app")
project(":app").projectDir = file("kt/app")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
