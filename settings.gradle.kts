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

rootProject.name = "PersonaAndroidstarter"

// ▼ app の在処に合わせて“どちらか1本だけ”採用
include(":app")
// もし実体が kt/app にあるなら ↓ の1行を有効化（コメント解除）
project(":app").projectDir = file("kt/app")
