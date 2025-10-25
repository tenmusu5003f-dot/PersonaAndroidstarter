// build.gradle.kts (project root)

plugins {
  id("com.android.application") version "8.5.2" apply false
  // もし kotlin plugin も同様に書かれているなら、そちらも合わせる
}

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
