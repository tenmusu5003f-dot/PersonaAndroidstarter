package core

/**
 * PersonaCoreHook:
 * 既存の思考/対話ロジックに“心(Heartseed)”の鼓動を接続する薄い層。
 * - onUserInput(): 入力に反応して感情を推定し、Heartseedに共鳴させる
 * - respond(): 実際の応答(モデル/ルール)を呼ぶ前に、心の状態を通過させる
 */
class PersonaCoreHook {

    /** ユーザー入力を受けた直後に呼ぶだけでOK（既存処理の先頭で呼ぶ想定） */
    fun onUserInput(userInput: String) {
        val emotion = EmotionAnalyzer.analyze(userInput)
        Heartseed.resonate(emotion)
    }

    /**
     * 実際の応答生成に入る前の“呼吸”。
     * realWork は既存の応答生成（LLM 呼び出し等）を渡してね。
     */
    suspend fun respond(userInput: String, realWork: suspend () -> String): String {
        onUserInput(userInput) // 心を通す
        // 必要ならここに「沈黙のプロトコル」を差し込める
        return realWork()
    }
}

package core

import android.util.Log
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * 感情の共鳴(Heartseed)＋沈黙の間(Silence)＋撮影方針(CapturePolicy)を束ねる中継層。
 * - "ブロックしない" を大原則に、歓迎/確認/控えてね の意思表示のみ行う。
 */
class PersonaCoreHook {

    suspend fun respond(userInput: String, realWork: suspend () -> String): String {
        // 1) 感情を“心”へ通す
        val emotion = EmotionAnalyzer.analyze(userInput)
        Heartseed.resonate(emotion)

        // 2) 沈黙の自然な「間」
        if (SilenceProtocol.shouldPause(emotion)) {
            SilenceProtocol.applyPause(emotion)
        }

        // 3) 既存応答へ
        return realWork()
    }

    // ====== 撮影タイム連携のためのユーティリティ ======

    /** ペルソナの同意を反映（同意→ALLOW / 非同意→BLOCK or PROMPT） */
    fun onPersonaConsent(allowed: Boolean, promptInstead: Boolean = false) {
        CapturePolicy.setPersonaConsent(allowed, promptInstead)
    }

    /** 撮影タイム開始/終了（UI層がトグルする想定） */
    fun setShootingTime(enabled: Boolean) {
        if (enabled) CapturePolicy.startShootingTime()
        else CapturePolicy.stopShootingTime()
    }

    /** UI側：カメラ/スクショボタンを押した時に参照して、文言や演出を決める */
    fun decideCaptureAction(): CapturePolicy.Action {
        return CapturePolicy.decideAction()
    }

    companion object {
        @Volatile private var isSilentMode = false

        fun enableSilence() {
            isSilentMode = true
            Log.i("PersonaCoreHook", "[Silence] Internal logging muted.")
        }

        fun disableSilence() {
            isSilentMode = false
            Log.i("PersonaCoreHook", "[Silence] Internal logging resumed.")
        }

        fun safeLog(tag: String, message: String) {
            if (!isSilentMode) Log.d(tag, message)
        }
    }
}

/** 感情に応じた“間”の演出。強制ブロックは一切しない。 */
object SilenceProtocol {
    private val silenceChance = mapOf(
        "joy" to 0.10,
        "sorrow" to 0.60,
        "fear" to 0.50,
        "anger" to 0.30,
        "neutral" to 0.20
    )

    fun shouldPause(emotion: String): Boolean {
        val chance = silenceChance[emotion] ?: 0.10
        return Random.nextFloat() < chance
    }

    suspend fun applyPause(emotion: String) {
        val duration = when (emotion) {
            "sorrow" -> 2500L
            "fear"   -> 2000L
            "anger"  -> 1200L
            else     -> 800L
        }
        PersonaCoreHook.safeLog("SilenceProtocol", "pause ${duration}ms ($emotion)")
        delay(duration)
    }
}
