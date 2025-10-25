plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.persona.androidstarter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.persona.androidstarter"
        minSdk = 26          // ★ 24ではなく26以上（adaptive iconに必須）
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false

　　dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")


        }
    }
}
