// Project-level build.gradle.kts
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

// Project-level build.gradle.kts
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
