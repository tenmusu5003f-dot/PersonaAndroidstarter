plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.persona"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.persona"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    // ★ Java/Kotlin のターゲットを統一（JDK17想定）
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // D8/R8 の並列度を上げたい時は Gradle 側の並列設定で十分
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xjvm-default=all"   // デフォルトメソッド最適化（安全な範囲）
        )
    }

    buildTypes {
        release {
            // ★ APKサイズ最適化（ビルドもやや速くなる）
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // デバッグは素直に（速さ重視でOK）
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }

    packaging {
        // 余計なメタ情報を除外して I/O を減らす
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE*,NOTICE*}"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}