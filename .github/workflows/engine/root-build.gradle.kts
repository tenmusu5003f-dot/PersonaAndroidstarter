// --- root-build.gradle.kts (final version) ---
plugins { /* root は空でOK。依存解決のみ */ }

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// 👇 これを追加しないと "Task 'clean' not found" が出る
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
