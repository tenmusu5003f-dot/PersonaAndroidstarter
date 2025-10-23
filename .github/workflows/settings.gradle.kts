rootProject.name = "PersonaAndroidStarter"

// モジュール名は一般的な :app に固定して、物理フォルダを「アプリ」にマッピング
include(":app")
project(":app").projectDir = file("アプリ")
