plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.10"
    application
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":persona_core"))
    implementation(compose.desktop.currentOs)
}

application {
    mainClass.set("com.example.persona.desktop.MainKt")
}
