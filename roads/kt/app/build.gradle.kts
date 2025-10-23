plugins {
    id 'org.jetbrains.kotlin.android'
}

// --- roadsV1: simple duplicate scan (source filenames) ---
tasks.register("scanDuplicateSourceNames") {
    group = "verification"
    description = "Scan kotlin source for duplicate filenames (warning only)."
    doLast {
        val src = fileTree("src/main/kotlin") { include("**/*.kt") }.files
        val byName = src.groupBy { it.name }
        val dups = byName.filter { it.value.size > 1 }
        if (dups.isNotEmpty()) {
            println("⚠️ Duplicate source filenames detected:")
            dups.forEach { (name, files) ->
                println(" - $name")
                files.forEach { f -> println("    • ${f.relativeTo(project.projectDir)}") }
            }
        } else {
            println("✅ No duplicate source filenames.")
        }
    }
}
// CI で実行したいとき：
// tasks.named("check").configure { dependsOn("scanDuplicateSourceNames") }

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "core.ui"
    compileSdk = 34

    defaultConfig {
        applicationId = "core.ui"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
}
