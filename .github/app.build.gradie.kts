plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.persona.androidstarter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.persona.androidstarter"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug { isMinifyEnabled = false }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Navigation/Compose等を使うならここに追加（後でOK）
}

android {
  buildTypes {
    debug {
      buildConfigField("boolean","OPT_EMULATOR","true")
      buildConfigField("boolean","OPT_INTEGRITY","false")
      buildConfigField("boolean","OPT_HOOK_DEEP","false")
    }
    release {
      buildConfigField("boolean","OPT_EMULATOR","false")
      buildConfigField("boolean","OPT_INTEGRITY","false")
      buildConfigField("boolean","OPT_HOOK_DEEP","false")
      // R8が未使用コードを消しやすい設定
      isMinifyEnabled = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}
