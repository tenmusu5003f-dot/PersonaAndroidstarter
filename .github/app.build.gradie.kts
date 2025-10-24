plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "com.persona.androidstarter"
  compileSdk = 34
  defaultConfig {
    applicationId = "com.persona.androidstarter"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "0.1"
  }
  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }
}

dependencies {
  implementation(platform("androidx.compose:compose-bom:2024.06.00"))
  implementation("androidx.activity:activity-compose:1.9.0")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
  implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
  implementation("androidx.security:security-crypto:1.1.0-alpha06")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
}
