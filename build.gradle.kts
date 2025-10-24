plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.persona"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.persona"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}
