package com.persona.androidstarter.security

import android.content.Context
import kotlinx.coroutines.*

object TTSPrewarm {
  fun warm(ctx: Context) {
    GlobalScope.launch(Dispatchers.IO) {
      try {
        // 外部 TTS ライブラリの初期化例
        // Example: val tts = ExternalTTS(ctx); tts.preloadVoice("default")
        // ここは実際に入れてる TTS SDK に合わせて実装
      } catch (e: Exception) {
        // 失敗してもフォールバック
      }
    }
  }
}
