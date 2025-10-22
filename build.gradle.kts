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

plugins {
    // ルートは空でOK（各サブプロジェクトに記述）
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "17"
    }
}
