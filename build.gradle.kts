// ルートの build.gradle.kts
// ───────────────────────────────
// Android Studio Electric Eel 以降対応
// GitHub Actions でもそのまま動く構成
// ───────────────────────────────

plugins {
    // Android アプリ用プラグイン
    id("com.android.application") version "8.6.1" apply false

    // Kotlin for Android
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false

    // Kotlin Serialization（必要に応じて）
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24" apply false

    // Kotlin Kapt（アノテーションプロセッサー）
    id("org.jetbrains.kotlin.kapt") version "1.9.24" apply false

    // Hilt（依存性注入）
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    // Google Services（Firebase 等で使用する場合）
    id("com.google.gms.google-services") version "4.4.2" apply false

    // Crashlytics（Firebase用、任意）
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}
