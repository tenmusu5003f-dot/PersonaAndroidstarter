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

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.9.23"
    id("com.android.library")
}

kotlin {
    androidTarget()

    // iOS（フレームワーク出力／将来Swiftから利用）
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm("desktop") // Desktop用
    js(IR) { browser() }

    sourceSets {
        val commonMain by getting
        val commonTest by getting

        val androidMain by getting
        val androidUnitTest by getting

        val iosMain by getting
        val iosTest by getting

        val desktopMain by getting
        val desktopTest by getting

        val jsMain by getting
        val jsTest by getting
    }
}

android {
    namespace = "com.example.persona.core"
    compileSdk = 34
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
