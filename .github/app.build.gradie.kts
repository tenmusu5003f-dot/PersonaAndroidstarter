plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.persona.sample"          // ← 固有のパッケージ名に
    compileSdk = 34

    defaultConfig {
        applicationId = "com.persona.sample"   // ← 同上
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // 必要ならdebug専用設定
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true   // 必要に応じて
        // compose = true     // ← Compose使う場合はtrueに（下の依存も有効化）
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // ↓ Composeを使う場合のみ（buildFeatures.compose = true も忘れずに）
    // val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    // implementation(composeBom)
    // androidTestImplementation(composeBom)
    // implementation("androidx.compose.ui:ui")
    // implementation("androidx.compose.material3:material3")
    // implementation("androidx.activity:activity-compose:1.9.2")
    // debugImplementation("androidx.compose.ui:ui-tooling")
    // implementation("androidx.compose.ui:ui-tooling-preview")
}
