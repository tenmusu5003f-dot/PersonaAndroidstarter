# PersonaAndroidStarter — サイズ最適化メモ

1) **AABで配布**（Playは自動スプリット）。サイドロードは `splits` 有効のAPKを使う。
2) **minifyEnabled + shrinkResources** をリリースで有効（本リポ済み）。
3) **ABIは arm64-v8a のみ**（他ABIは必要に応じて追加）。
4) **言語は ja / en のみ**（設定画面に増やす場合だけ追加）。
5) 画像は **WebP/AVIF** 推奨（PNGはbuildでcrunch）。
6) BGMは `raw/` に入れる場合、**3本/ペルソナまで**を推奨。外部ストレージ差替えにも対応可。
7) 依存は `compose-bom` で固定。未使用モジュールは入れない。
8) R8は**fullMode**、ログ除去済み。

> 実機での概算：コード主体の本プロジェクトは**数MB台**で収まる想定。素材を追加する場合も、
> 1曲あたり ~1–3MB（AAC/OGG）×必要分に抑えると、全体で20MB未満を狙える。


---

## 追加：外部BGM／画像最適化／解析ビルド

- **外部BGM**：端末内 `filesDir/music/Persona/<ID>/calm.mid.bright.ogg` を配置すると、
  同名スロットが **rawより優先** で再生されます（例：`ECHO/calm.ogg`）。
- **画像最適化**：`cwebp` がある環境で `./gradlew convertPngToWebp` を実行すると
  `res/` 配下のPNGを**品質82のWebP**に変換（元PNGは削除）。
- **解析ビルド**：`./gradlew :app:assembleAnalyze` でR8の
  `usage.txt / mapping.txt / configuration.txt` を `app/build/outputs/proguard/` に出力。
  ProGuardの `-whyareyoukeeping` で**保持理由の調査**が可能。


### 外部BGMインポート（アプリ内）
- 設定エリアに**外部BGMインポート**画面を追加。
- ペルソナとスロット（calm/mid/bright）を選び、**端末内の音源を選択**→`filesDir/music/Persona/<ID>/`へ保存。
- 再生は**外部が優先**され、アプリ本体サイズにBGMを含めなくてよい。


### 追加：サイズ監査 & 仕上げ
- `./gradlew sizeReport`：**上位大型ファイル**と**拡張子別合計**を一覧化。
- `./gradlew cleanPreviews`：`*Preview.kt` / `*Sample*.kt` を**削除**（必要時のみ。実行前にGit管理を推奨）。
- `buildFeatures.buildConfig=false`：`BuildConfig.class` を出荷から省略。

**推奨フロー**
1. `sizeReport` で大物を把握 → 画像は WebP、BGMは外部化。
2. プレビュー削除は**必要な時のみ**（R8でも多くは除去されるため普段は不要）。
