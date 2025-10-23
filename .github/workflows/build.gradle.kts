// ルート用の極小 Gradle ビルド（CIが読み込むだけの足場）
plugins {
    // ここは空でもOK。依存解決だけ行う
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
