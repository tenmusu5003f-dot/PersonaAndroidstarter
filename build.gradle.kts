plugins {
  id("com.android.application") version "8.1.0" apply false
  id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

android {
  packagingOptions {
    resources {
      excludes += setOf("**/*.apk", "**/*.aab", "**/*.bak", "**/*.tmp")
    }
  }
}

val includeExtraAssets by tasks.registering(Copy::class) {
  from(file("$rootDir/external_assets"))
  include("**/*.script", "**/*.tts") // 含めたい拡張子
  into(file("$buildDir/generated/assets/my_includes"))
}
android {
  // ...
  sourceSets["main"].assets.srcDir("$buildDir/generated/assets/my_includes")
}
tasks.named("preBuild") { dependsOn(includeExtraAssets) }
