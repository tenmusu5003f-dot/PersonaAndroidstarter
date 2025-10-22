


---

README.md

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

2) ビルド（ローカル）

./gradlew assembleDebug
# Windows の場合: gradlew.bat assembleDebug

3) 実機インストール（adb）

adb install -r app/build/outputs/apk/debug/app-debug.apk


---

🤖 GitHub Actions でのビルド

対象ワークフロー: .github/workflows/android.yml

トリガー: push / pull_request（main）


成果物の取得手順

1. GitHub > Actions > 最新の build ジョブを開く


2. Artifacts の app-debug をダウンロード（ZIP）




---

📁 プロジェクト構成

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


---

🔐 セキュリティ方針（要点）

最小権限の原則：不要なパーミッションは宣言しない

署名鍵：公開リポジトリには置かない。配布時は Play App Signing を利用

CI サンドボックス：Secrets を使う場合は環境分離し、ログへ出力しない

依存解決：google(), mavenCentral() のみ使用



---

🧰 トラブルシュート

🔸 Inconsistent JVM-target（Java 1.8 vs Kotlin 17）

android { compileOptions { sourceCompatibility/targetCompatibility = JavaVersion.VERSION_17 } }

kotlinOptions { jvmTarget = "17" } を app/build.gradle.kts に設定


🔸 ./gradlew: No such file or directory

ルートに gradlew と gradle/wrapper/gradle-wrapper.jar が存在するか確認

ない場合はローカルで gradle wrapper を実行してコミット

実行権限付与（ローカル）

chmod +x gradlew


🔸 Action の警告: gradle/gradle-build-action は非推奨

置き換え済み: gradle/actions/setup-gradle@v3


🔸 CI のキャッシュ警告が出る

ビルドは成功なら問題なし（速度最適化の警告）。時間ができたらキャッシュキーを調整。



---

📝 ライセンス

MIT または任意（ここに記載）


---

🙏 謝辞

Android & Kotlin チーム

GitHub Actions メンテナ

共同開発のみなさん


---

## 置き換える箇所メモ
- `<YOUR_USER>` と `<YOUR_REPO>` を自分のものに差し替え。
- ライセンス文言をあなたの意図に合わせて更新。



