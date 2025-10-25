pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PersonaAndroidstarter"

// モジュールパスが kt/app の場合はこちらを使う
// project(":app").projectDir = file("kt/app")
include(":app")

rootProject.name = "PersonaAndroidstarter"

project(":app").projectDir = file("kt/app")
include(":app")
