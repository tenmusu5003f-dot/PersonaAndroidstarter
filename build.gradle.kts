plugins {
    id("com.android.application") version "8.3.2" apply true
    kotlin("android") version "1.9.24"
}

android {
    namespace = "com.persona.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.persona.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
}
