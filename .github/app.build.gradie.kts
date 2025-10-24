plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "com.persona"
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
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
}
dependencies {
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
  implementation("androidx.activity:activity-ktx:1.9.2")
}
