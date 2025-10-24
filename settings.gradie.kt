pluginManagement {
    repositories {
        google()       // ← これがないと Android Plugin が見つからない！
        mavenCentral()
        gradlePluginPortal()
    }
}

pluginManagement {
  repositories { gradlePluginPortal(); google(); mavenCentral() }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories { google(); mavenCentral() }
}
rootProject.name = "PersonaAndroidstarter"
include(":app")
