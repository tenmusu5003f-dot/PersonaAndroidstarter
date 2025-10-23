// <project-root>/settings.gradle.kts
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
    }
}

rootProject.name = "PersonaAndroidstarter"
include(":app")
project(":app").projectDir = file("_roads/kt/app")

rootProject.name = "PersonaAndroidstarter"

// ← ここ重要！ app の実パスを明示
include(":app")
project(":app").projectDir = file("_roads/kt/app")
