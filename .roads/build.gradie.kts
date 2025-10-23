// ルート build.gradle.kts
plugins {
    // 通常は空。全体共通プラグインを使うときだけ書く
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
