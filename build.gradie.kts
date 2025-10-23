// <project-root>/build.gradle.kts
plugins {
    id("com.android.application") version "8.6.1" apply false
    kotlin("android") version "2.0.20" apply false
}

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
    versionName = "1.0"
  }

  // ← デフォルトの src 配下に置く場合はこのままでOK
  // もし今の「_roads/kt/...」構成を維持したいなら下のブロックを有効化:
  /*
  sourceSets["main"].java.srcDirs("_roads/kt/app/src/main/kotlin")
  sourceSets["main"].manifest.srcFile("_roads/kt/app/src/main/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("_roads/kt/app/src/main/res")
  */
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("androidx.core:core-ktx:1.13.1")
  implementation("androidx.appcompat:appcompat:1.7.0")
  implementation("com.google.android.material:material:1.12.0")
}
