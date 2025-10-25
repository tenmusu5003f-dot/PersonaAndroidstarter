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

// app モジュールがリポジトリの別パスにある場合はパスを指定する:
// project(":app").projectDir = file("kt/app")

include(":app")