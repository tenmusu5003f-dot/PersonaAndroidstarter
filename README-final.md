# PersonaAndroidStarter v5.0 — Final Optimize Build

## すぐ遊ぶために
1. ビルド（AAB推奨）：`./gradlew :app:bundleRelease`
2. 起動するとスプラッシュに **「ようこそ、ペルソナの庭へ。」** と表示 → 初回だけBGMを端末内に展開
3. ホーム→「作成」で遊べる。設定から QuietHours / BGM / プライバシーを調整可能

## 収録要素
- ペルソナ別BGM（初回自動展開）
- ナビゲーション統合（Home / Compose / Settings）
- QuietHours／平日・週末通知／天気・時間帯連動／季節演出
- 端末内学習・PG-13本番安全／成人は内部限定・ミヨ緑（内部）

## サイズ最適化
- R8 fullMode・リソース縮小・arm64 split・BuildConfig無効
- preBuildで**画像縮小＋WebP化**
- 外部BGM優先（本体サイズを増やさない）
