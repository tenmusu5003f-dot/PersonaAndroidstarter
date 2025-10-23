package core

/**
 * アプリ全体の軽量なコア。ここで「撮影可否」などのグローバル方針を握る。
 * UI側は PersonaCore.onScreenshotAttempt() を見て分岐するだけ。
 */
object PersonaCore {
    @Volatile
    private var allowScreenshots: Boolean = false

    fun enableScreenshots() { allowScreenshots = true }
    fun disableScreenshots() { allowScreenshots = false }

    /** スクショを試みる前に呼ぶ。trueなら許可、falseなら抑止UIへ。 */
    fun onScreenshotAttempt(): Boolean = allowScreenshots
}
