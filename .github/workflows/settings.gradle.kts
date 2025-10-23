gradle init \

--type java-application \

--dsl kotlin \

--test-framework junit-jupiter \

--package my.project \

--project-name my project \

--no-split-project \

--java-version 17

rootProject.name = "PersonaAndroidStarter"

// モジュール名は一般的な :app に固定して、物理フォルダを「アプリ」にマッピング
include(":app")
project(":app").projectDir = file("アプリ")
