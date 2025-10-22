plugins {
    id("org.jetbrains.kotlin.js") version "1.9.23"
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
        }
    }
    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":persona_core"))
            }
        }
    }
}
