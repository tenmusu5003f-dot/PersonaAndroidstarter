plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
        }
    }

    // 🧠 高速化設定
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }

    // 💨 キャッシュ・最適化
    packaging {
        resources.excludes += setOf(
            "META-INF/LICENSE*", "META-INF/DEPENDENCIES", "META-INF/NOTICE*"
        )
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
}
