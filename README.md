# PersonaAndroidstarter

> **最小構成の Android/Kotlin サンプル**  
> GitHub Actions で `assembleDebug` を自動ビルド & APK を成果物として配布します。  
> セキュア設計（最小権限・署名分離・CI サンドボックス）を重視。

---

## ✅ ステータス

- ブランチ: `main`  
- CI: ![Android CI](https://github.com/<YOUR_USER>/<YOUR_REPO>/actions/workflows/android.yml/badge.svg)

> バッジの URL は自分のユーザー名/リポジトリ名に置き換えてください。

---

## 🧩 機能

- Kotlin / Android Gradle Plugin ベース
- `minSdk 24`, `targetSdk 34`
- GitHub Actions で自動ビルド & APK アーティファクト化
- プロガード設定（release の最小例）
- 将来の Play ストア提出を見据えた構成

---

## 🛠 動作要件（ローカルで触る場合）

- Android Studio Giraffe 以降
- JDK 17（Temurin 推奨）
- Android SDK 34

> GitHub Actions 側では自動でセットアップされます。

---

## 🚀 クイックスタート

### 1) 取得
```bash
git clone https://github.com/<YOUR_USER>/<YOUR_REPO>.git
cd <YOUR_REPO>

./gradlew assembleDebug
# Windows の場合: gradlew.bat assembleDebug

adb install -r app/build/outputs/apk/debug/app-debug.apk

.
├─ app/
│  ├─ src/main/
│  │  ├─ AndroidManifest.xml
│  │  ├─ java/...        # コード
│  │  └─ res/...         # リソース
│  └─ build.gradle.kts   # アプリ用 Gradle
├─ build.gradle.kts      # ルート（ある場合）
├─ settings.gradle.kts   # ルート設定（ある場合）
└─ .github/workflows/android.yml  # CI

chmod +x gradlew
