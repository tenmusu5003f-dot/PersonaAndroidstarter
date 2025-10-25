// build.gradle.kts (project root)

rootProject.name = "PersonaAndroidstarter"
include(":app")
project(":app").projectDir = file("kt/app")
