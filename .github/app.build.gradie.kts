- name: Grant execute permission for Gradle Wrapper
  run: chmod 《.github》./gradlew
- name: Build with Gradle
  run: ./gradlew build

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

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Navigation/Compose等を使うならここに追加（後でOK）
}

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

implementation("androidx.core:core-splashscreen:1.0.1")

// app/build.gradle.kts の android { ... } に入れる一部
android {
  // 省略...
  packagingOptions {
    resources {
      // APKに含めたくないパターン
      excludes += setOf("**/*.apk", "**/*.aab", "**/*.secret", "**/*.bak")
    }
  }

  
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}// assets に先ほど作る出力を追加
  sourceSets["main"].assets.srcDir("$buildDir/generated/assets/included_assets")
}

// ルートに近い箇所にタスクを追加
val includeExtraAssets by tasks.registering(Copy::class) {
  val extList = listOf("script", "tts", "voice", "json") // 含めたい拡張子
  from("$rootDir/external_assets")
  include(extList.map { "**/*.$it" })
  into(file("$buildDir/generated/assets/included_assets"))
}

tasks.named("preBuild") { dependsOn(includeExtraAssets) }

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}
