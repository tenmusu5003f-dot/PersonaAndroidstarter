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
        versionName = "1.0.0"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }

    packaging {
        resources.excludes += setOf("META-INF/LICENSE*", "META-INF/NOTICE*")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    // Compose UI
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")

    // WorkManager（夜間学習・最適化）
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Lifecycle（必要最低限）
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
}

dependencies {
    // 既存 …
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.8")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")

    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

    // ▼ 追加：設定保存に DataStore を使う（SharedPreferencesより安全）
    implementation("androidx.datastore:datastore-preferences:1.1.1")
}
