// settings.gradle.kts (project root)

pluginManagement {
    repositories {
        // ← ここが無いと com.android.application が見つからない
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

rootProject.name = "PersonaAndroidstarter"

// モジュールは kt/app にあるので、これで :app として認識させる
include(":app")
project(":app").projectDir = File("kt/app")
