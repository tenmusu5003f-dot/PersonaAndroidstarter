name: Android CI

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]
  workflow_dispatch:

jobs:
  build:
    name: Build Debug APK
    runs-on: ubuntu-latest

    steps:
      # 1. コードを取得
      - name: Checkout
        uses: actions/checkout@v4

      # 2. JDK 17 をセットアップ
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "17"

      # 3. Android SDK をインストール
      - name: Install Android SDK
        run: |
          sudo apt-get update
          sudo apt-get install -y wget unzip
          wget https://dl.google.com/android/repository/commandlinetools-linux-10406996_latest.zip
          mkdir -p $HOME/android-sdk/cmdline-tools
          unzip commandlinetools-linux-10406996_latest.zip -d $HOME/android-sdk/cmdline-tools
          yes | $HOME/android-sdk/cmdline-tools/bin/sdkmanager --licenses
          $HOME/android-sdk/cmdline-tools/bin/sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

      # 4. gradlew に実行権限を付与
      - name: Make gradlew executable
        run: chmod +x ./gradlew

      # 5. Debug APK をビルド
      - name: Build Debug APK
        run: ./gradlew :app:assembleDebug --stacktrace

      # 6. 成果物（APK）を保存
      - name: Upload APK Artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk

