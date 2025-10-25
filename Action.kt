./gradlew --stop
./gradlew clean
./gradlew :app:assembleDebug --stacktrace
# 失敗時は最後の 50–100 行ログを読む（原因層を特定：Gradle/Manifest/Res/Java）

/ (repo root)
├─ settings.gradle.kts       
├─ build.gradle.kts           
├─ gradle.properties          
├─ .github/…                  
├─ LICENSE.md / README*.md 
└─ kt/
   └─ app/
      ├─ build.gradle.kts     
      ├─ proguard-rules.pro  
      └─ src/
         └─ main/
            ├─ AndroidManifest.xml
            ├─ java/
            │  └─ com/persona/androidstarter/
            │     ├─ MainActivity.kt
            │     ├─ audio/
            │     │  ├─ AudioMonitor.kt
            │     │  ├─ AudioRouter.kt
            │     │  └─ AudioSafetyLog.kt
            │     ├─ security/ …（CryptoVault 等）
            │     ├─ sos/ …（SOS 関連）
            │     └─ location/ …（位置関連）
            └─ res/
               ├─ layout/activity_main.xml
               ├─ values/strings.xml
               └─ mipmap-*/ic_launcher.png
