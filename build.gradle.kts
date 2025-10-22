plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.persona.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.persona.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "0.2"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{chr,chr}.kotlin_module"
        }
    }
}

dependencies {
    // ML Kit Text Recognition (on-device)
    implementation("com.google.mlkit:text-recognition:16.0.0")
    implementation("com.google.mlkit:text-recognition-japanese:16.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Android Keystore crypto
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    implementation("com.google.accompanist:accompanist-permissions:0.36.0")

    // Location
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Config storage
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    val composeBom = platform("androidx.compose:compose-bom:2024.09.02")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Networking & TLS pinning
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // WorkManager for background learning jobs
    implementation("androidx.work:work-runtime-ktx:2.9.1")
}
