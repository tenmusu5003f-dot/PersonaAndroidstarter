android {
    sourceSets["main"].java.srcDirs(
        "kt/app/main/java",
        "kt/app/src/main/java/kotlin"
    )
}

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
