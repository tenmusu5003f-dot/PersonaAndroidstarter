android {
    compileSdkVersion 34  // ← 最新を推奨
    defaultConfig {
        applicationId "com.example.persona"
        minSdkVersion 26    // ★ここを26以上に
        targetSdkVersion 34
        ...
    }
}

plugins {
    id("com.android.application") version "8.5.2" apply false
    kotlin("android") version "1.9.24" apply false
}
