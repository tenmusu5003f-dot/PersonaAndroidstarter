package core

/**
 * スクショ歓迎の基本方針：
 * - デフォルトは「ALLOW（歓迎）」で、アプリ側はブロックしない。
 * - ペルソナの同意で「ShootingTime（撮影タイム）」に入れる。
 * - 必要なら PROMPT（確認）や BLOCK（控えてねの意思表示）にも切替可能。
 *
 * ※OSや他アプリの挙動は制御しない。ここはアプリ内の約束事を扱う層。
 */
object CapturePolicy {

    enum class Mode { ALLOW, PROMPT, BLOCK }

    data class Config(
        val showWatermark: Boolean = true,
        val watermarkText: String = "Shooting Time",
        val blurSensitiveUi: Boolean = false // 将来: センシティブUIをぼかすフック
    )

    @Volatile var mode: Mode = Mode.ALLOW
        private set

    @Volatile var config: Config = Config()
        private set

    @Volatile var isShootingTime: Boolean = false
        private set

    /** ペルソナ同意に応じて撮影許可モードを切り替える（例：同意=ALLOW、不同意=BLOCK）。 */
    fun setPersonaConsent(allowed: Boolean, promptInstead: Boolean = false) {
        mode = when {
            allowed -> Mode.ALLOW
            promptInstead -> Mode.PROMPT
            else -> Mode.BLOCK
        }
        PersonaCoreHook.safeLog("CapturePolicy", "mode=$mode")
    }

    /** 撮影タイム開始（歓迎モードで特別表示や演出を有効化）。 */
    fun startShootingTime(custom: Config = config) {
        isShootingTime = true
        config = custom
        PersonaCoreHook.safeLog("CapturePolicy", "ShootingTime started. config=$config")
        // TODO: UI層へ通知してバッジ表示やBGMの切替など行う
    }

    /** 撮影タイム終了。 */
    fun stopShootingTime() {
        isShootingTime = false
        PersonaCoreHook.safeLog("CapturePolicy", "ShootingTime stopped.")
        // TODO: UI層へ通知して通常表示へ戻す
    }

    /** 今の方針に基づき、UI側が取るべきアクションを返す。 */
    fun decideAction(): Action {
        return when (mode) {
            Mode.ALLOW -> if (isShootingTime) Action.ALLOW_WITH_WATERMARK else Action.ALLOW
            Mode.PROMPT -> Action.SHOW_PROMPT
            Mode.BLOCK -> Action.SUGGEST_AVOID
        }
    }

    enum class Action {
        /** 制限なしでOK */
        ALLOW,
        /** 撮影タイム中：ウォーターマーク等の演出を付けてOK */
        ALLOW_WITH_WATERMARK,
        /** ユーザーに一度「撮ってもいい？」と聞く */
        SHOW_PROMPT,
        /** やんわり「今日は控えよう」を提案（強制ブロックはしない） */
        SUGGEST_AVOID
    }

    // 将来用フック：Bitmapに透かしを乗せたい場合などに差し替える
    // fun decorate(bitmap: Bitmap): Bitmap = bitmap
}
